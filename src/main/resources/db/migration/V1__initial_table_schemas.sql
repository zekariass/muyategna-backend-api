-- V1__user_management_schema_1.sql (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS postgis; -- Enable PostGIS

CREATE TYPE data_type_enum AS ENUM ('INTEGER', 'DOUBLE', 'BOOLEAN', 'STRING');

-- Country Table
CREATE TABLE IF NOT EXISTS country (
    country_id BIGSERIAL PRIMARY KEY,            -- Unique ID for country
    name VARCHAR(100) NOT NULL UNIQUE,           -- Name of the country (e.g., 'Ethiopia')
    iso_code2 CHAR(2) NOT NULL UNIQUE,           -- 2-letter ISO code (e.g., 'ET')
    iso_code3 CHAR(3) UNIQUE,                    -- 3-letter ISO code (e.g., 'ETH') optional
    iso_code_numeric CHAR(3) UNIQUE,             -- 3-letter ISO code (e.g., 'ETH') optional
    is_global BOOLEAN NOT NULL DEFAULT FALSE,    -- Is this a global country?
    continent VARCHAR(100),                      -- Continent (e.g., 'Africa')
    description TEXT,                            -- Description or additional information about the country
    taxpayer_id_type VARCHAR(50) NOT NULL,              -- Type of taxpayer ID (e.g., 'TIN', 'VAT')
    currency VARCHAR(3) NOT NULL,                        -- Currency used in the country (e.g., 'ETB' for Ethiopian Birr)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp of last update
);

CREATE INDEX idx_name ON country(name);
CREATE INDEX idx_country_iso_code2 ON country(iso_code2);
CREATE UNIQUE INDEX idx_only_one_global_country ON country ((is_global)) WHERE is_global IS TRUE;

CREATE TABLE IF NOT EXISTS language (
    language_id BIGSERIAL PRIMARY KEY,
    country_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    locale VARCHAR(10) NOT NULL UNIQUE, -- Optional locale (e.g., 'en_US', 'am_ET')
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_global BOOLEAN NOT NULL DEFAULT FALSE,
    native_name VARCHAR(50),
    direction VARCHAR(10),
    flag_emoji VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_language_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE CASCADE,
    CONSTRAINT unique_language_country_code UNIQUE (country_id, code) -- Ensure unique language codes per country
);

-- Create an index on the 'code' column for fast lookups by language code
CREATE UNIQUE INDEX idx_language_code ON language(code);
CREATE INDEX idx_language_is_default ON language(is_default);
CREATE INDEX idx_language_locale ON language(locale);
CREATE UNIQUE INDEX idx_unique_default_language_per_country ON language (country_id) WHERE is_default IS TRUE;
CREATE UNIQUE INDEX idx_only_one_global_language ON language ((is_global)) WHERE is_global IS TRUE;


CREATE TABLE IF NOT EXISTS country_translation (
    translation_id BIGSERIAL PRIMARY KEY,
    country_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    continent VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_country_translation_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE CASCADE,
    CONSTRAINT fk_country_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE
);

-- Indexes for faster lookups by country and language
CREATE INDEX idx_country_translation_country_id ON country_translation(country_id);
CREATE INDEX idx_country_translation_language_id ON country_translation(language_id);

-- Optional composite index if you often query by both country and language together
CREATE UNIQUE INDEX idx_country_translation_country_id_language_id ON country_translation(country_id, language_id);


-- Region Table
CREATE TABLE IF NOT EXISTS region (
    region_id BIGSERIAL PRIMARY KEY,             -- Unique ID for region
    country_id BIGINT NOT NULL,               -- Foreign Key to the Country table
    name VARCHAR(100) NOT NULL,                  -- Name of the region (e.g., 'California')
    description TEXT,                            -- Description of the region
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of last update
    CONSTRAINT fk_region_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE CASCADE,
    CONSTRAINT unique_region_name_country_id UNIQUE (name, country_id)
);

CREATE INDEX idx_region_country_id ON region(country_id);


CREATE TABLE IF NOT EXISTS region_translation (
    translation_id BIGSERIAL PRIMARY KEY,
    region_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_region_translation_region FOREIGN KEY (region_id) REFERENCES region(region_id) ON DELETE CASCADE,
    CONSTRAINT fk_region_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE
);

-- Indexes for faster lookup by region and language
CREATE INDEX idx_region_translation_region_id ON region_translation(region_id);
CREATE INDEX idx_region_translation_language_id ON region_translation(language_id);

-- Unique composite index to ensure one translation per language per region
CREATE UNIQUE INDEX idx_region_translation_region_id_language_id ON region_translation(region_id, language_id);


-- City Table
CREATE TABLE IF NOT EXISTS city (
    city_id BIGSERIAL PRIMARY KEY,               -- Unique ID for city
    region_id BIGINT NOT NULL,                   -- Foreign Key to the Region table
    name VARCHAR(100) NOT NULL,             -- Name of the city (e.g., 'Addis Ababa')
    description TEXT,                            -- Description of the city
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of last update
    CONSTRAINT fk_city_region FOREIGN KEY (region_id) REFERENCES region(region_id) ON DELETE CASCADE, -- Reference to Region table
    CONSTRAINT unique_city_name_region_id UNIQUE (name, region_id) -- Ensure unique city names within a region
);

CREATE INDEX idx_city_region_id ON city(region_id);
CREATE INDEX idx_city_city_name ON city(name);


CREATE TABLE IF NOT EXISTS city_translation (
    translation_id BIGSERIAL PRIMARY KEY,
    city_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_city_translation_city FOREIGN KEY (city_id) REFERENCES city(city_id) ON DELETE CASCADE,
    CONSTRAINT fk_city_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE
);


-- Indexes for efficient queries on city and language
CREATE INDEX idx_city_translation_city_id ON city_translation(city_id);
CREATE INDEX idx_city_translation_language_id ON city_translation(language_id);

-- Unique composite index to ensure one translation per city-language pair
CREATE UNIQUE INDEX idx_city_translation_city_id_language_id ON city_translation(city_id, language_id);


-- Sub-city or division Table
CREATE TABLE IF NOT EXISTS sub_city_or_division (
    sub_city_or_division_id BIGSERIAL PRIMARY KEY,               -- Unique ID for city
    city_id BIGINT NOT NULL,                   -- Foreign Key to the Region table
    name VARCHAR(100) NOT NULL,             -- Name of the sub-city (e.g., 'Bole')
    description TEXT,                            -- Description of the city
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of last update
    CONSTRAINT sub_city_or_division_city_id FOREIGN KEY (city_id) REFERENCES city(city_id) ON DELETE CASCADE, -- Reference to Region table
    CONSTRAINT unique_sub_city_name_city_id UNIQUE (name, city_id) -- Ensure unique city names within a region
);

CREATE INDEX idx_sub_city_or_division_city_id ON sub_city_or_division(city_id);
CREATE INDEX idx_sub_city_or_division_name ON sub_city_or_division(name);


CREATE TABLE IF NOT EXISTS sub_city_or_division_translation (
    translation_id BIGSERIAL PRIMARY KEY,
    sub_city_or_division_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sub_city_or_division_translation_sub_city_or_division FOREIGN KEY (sub_city_or_division_id) REFERENCES sub_city_or_division(sub_city_or_division_id) ON DELETE CASCADE,
    CONSTRAINT fk_sub_city_or_division_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE
);

CREATE INDEX idx_sub_city_or_division_translation_sub_city_or_division_id ON sub_city_or_division_translation(sub_city_or_division_id);
CREATE INDEX idx_sub_city_or_division_translation_language_id ON sub_city_or_division_translation(language_id);

CREATE UNIQUE INDEX idx_sub_city_or_division_translation_sub_city_or_division_id_language_id ON sub_city_or_division_translation(sub_city_or_division_id, language_id);


-- Address Table
CREATE TABLE IF NOT EXISTS address (
    address_id BIGSERIAL PRIMARY KEY,           -- Unique ID for address
    country_id BIGINT NOT NULL,                 -- Foreign Key to Country table
    region_id BIGINT NOT NULL,                  -- Foreign Key to Region table
    city_id BIGINT NOT NULL,                    -- Foreign Key to City table
    sub_city_or_division_id BIGINT,                         -- Optional sub-city or district (foreign key to City)
    locality VARCHAR(255),                      -- Locality or neighborhood name
    street VARCHAR(255),                        -- Street name
    landmark VARCHAR(255),                      -- Landmark for easy identification
    postal_code VARCHAR(20),                    -- Postal code (e.g., '1234')
    geo_point GEOGRAPHY(POINT, 4326),
    full_address TEXT,                          -- Full address
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of address creation
    CONSTRAINT fk_address_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE CASCADE, -- Reference to Country table
    CONSTRAINT fk_address_region FOREIGN KEY (region_id) REFERENCES region(region_id) ON DELETE CASCADE, -- Reference to Region table
    CONSTRAINT fk_address_city FOREIGN KEY (city_id) REFERENCES city(city_id) ON DELETE CASCADE, -- Reference to City table
    CONSTRAINT fk_address_sub_city_or_division_id FOREIGN KEY (sub_city_or_division_id) REFERENCES sub_city_or_division(sub_city_or_division_id) -- Optional foreign key for sub-city
);

CREATE INDEX idx_address_country_id ON address(country_id);
CREATE INDEX idx_address_region_id ON address(region_id);
CREATE INDEX idx_address_city_id ON address(city_id);
CREATE INDEX idx_address_sub_city_or_division_id ON address(sub_city_or_division_id);
CREATE INDEX idx_address_postal_code ON address(postal_code);
CREATE INDEX idx_address_locality ON address(locality);
CREATE INDEX idx_address_geo_point ON address USING GIST (geo_point);



-- Create UserProfile Table
CREATE TABLE IF NOT EXISTS user_profile (
    user_profile_id BIGSERIAL PRIMARY KEY,
    keycloak_user_id UUID NOT NULL UNIQUE,
    first_name VARCHAR(100),
    middle_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    address_id BIGINT,
    profile_picture_url TEXT,
    default_language_id BIGINT NOT NULL, -- Foreign key to the language table
    last_login TIMESTAMP,
    referral_code VARCHAR(30),
    is_staff BOOLEAN DEFAULT FALSE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_profile_address FOREIGN KEY (address_id) REFERENCES address(address_id) ON DELETE SET NULL,
    CONSTRAINT fk_user_profile_language FOREIGN KEY (default_language_id) REFERENCES language(language_id) ON DELETE SET NULL
);

CREATE INDEX idx_user_profile_email ON user_profile(email);
CREATE INDEX idx_user_profile_keycloak_user_id ON user_profile(keycloak_user_id);
CREATE INDEX idx_user_profile_address_id ON user_profile(address_id);

--======================================================================================
--SERVICE RELATED TABLES
--======================================================================================
CREATE TABLE service_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_service_category_name ON service_category(name);


CREATE TABLE IF NOT EXISTS service_category_translation (
    id BIGSERIAL PRIMARY KEY,
    service_category_id INT NOT NULL,
    language_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sct_category FOREIGN KEY (service_category_id) REFERENCES service_category(id) ON DELETE CASCADE,
    CONSTRAINT fk_sct_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT unique_service_category_language UNIQUE (service_category_id, language_id)
);

CREATE INDEX idx_sct_language ON service_category_translation(language_id);
CREATE INDEX idx_sct_category ON service_category_translation(service_category_id);


CREATE TABLE IF NOT EXISTS service (
    id BIGSERIAL PRIMARY KEY,
    service_category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_service_service_category FOREIGN KEY (service_category_id) REFERENCES service_category(id) ON DELETE CASCADE
);

CREATE INDEX idx_service_service_category ON service(service_category_id);
CREATE INDEX idx_service_name ON service(name);


CREATE TABLE IF NOT EXISTS service_translation (
    id BIGSERIAL PRIMARY KEY,
    service_id INT NOT NULL,
    language_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_st_service FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE CASCADE,
    CONSTRAINT fk_st_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT uq_st_unique UNIQUE (service_id, language_id)
);

CREATE INDEX idx_st_service ON service_translation(service_id);
CREATE INDEX idx_st_language ON service_translation(language_id);

-- Create ENUM type for price_model
CREATE TYPE price_model_enum AS ENUM ('FIXED', 'HOURLY', 'TIERED', 'QUOTE', 'PER_UNIT');

CREATE TABLE IF NOT EXISTS service_country_availability (
    id BIGSERIAL PRIMARY KEY,
    service_id INT NOT NULL,
    country_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    price_model price_model_enum NOT NULL DEFAULT 'QUOTE', -- Default to QUOTE
    base_price NUMERIC(10, 2),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sca_service FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE CASCADE,
    CONSTRAINT fk_sca_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE CASCADE,
    CONSTRAINT uq_sca_service_country UNIQUE (service_id, country_id)
);

CREATE INDEX idx_sca_service_id ON service_country_availability(service_id);
CREATE INDEX idx_sca_country_id ON service_country_availability(country_id);
CREATE INDEX idx_sca_price_model ON service_country_availability(price_model);


--================================================================================
-- JOB QUESTION FLOW RELATED TABLES
--================================================================================
CREATE TABLE IF NOT EXISTS job_request_flow (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_request_question_flow_service FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_job_request_question_flow_service_id ON job_request_flow(service_id);
CREATE INDEX idx_job_request_question_flow_is_active ON job_request_flow(is_active);
CREATE UNIQUE INDEX uq_one_active_flow_per_service ON job_request_flow(service_id) WHERE is_active = TRUE;

-- Create ENUM type for question types
CREATE TYPE question_type_enum AS ENUM ('TEXT', 'NUMBER', 'BOOLEAN', 'DATE', 'SINGLE_SELECT', 'MULTI_SELECT');

-- Create JobRequestQuestion Table
CREATE TABLE IF NOT EXISTS job_question (
    id BIGSERIAL PRIMARY KEY,
    type question_type_enum NOT NULL,
    question TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE IF NOT EXISTS job_request_flow_question (
    id BIGSERIAL PRIMARY KEY,
    flow_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    order_index INT NOT NULL,
    is_start BOOLEAN NOT NULL DEFAULT FALSE,
    is_terminal BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_job_request_flow_question_flow FOREIGN KEY (flow_id) REFERENCES job_request_flow(id) ON DELETE CASCADE,
    CONSTRAINT fk_job_request_flow_question_question FOREIGN KEY (question_id) REFERENCES job_question(id) ON DELETE CASCADE,
    CONSTRAINT uq_flow_question UNIQUE (flow_id, question_id),
    CONSTRAINT uq_order_index UNIQUE (flow_id, order_index),
    CONSTRAINT chk_order_index_positive
    CHECK (
        order_index > 0
    ),
    CONSTRAINT chk_start_xor_terminal
    CHECK (
        NOT (is_start = TRUE AND is_terminal = TRUE)
    )
);

CREATE INDEX idx_job_request_flow_question_flow_id ON job_request_flow_question(flow_id);
CREATE INDEX idx_job_request_flow_question_question_id ON job_request_flow_question(question_id);
CREATE INDEX idx_job_request_flow_question_order_index ON job_request_flow_question(order_index);



-- job_question_translation
CREATE TABLE IF NOT EXISTS job_question_translation (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    helpText TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_question_translation_question FOREIGN KEY (question_id) REFERENCES Job_question(id) ON DELETE CASCADE,
    CONSTRAINT uq_job_question_language UNIQUE (question_id, language_id)
);

CREATE INDEX idx_question_translation_question_id ON job_question_translation (question_id);
CREATE INDEX idx_question_translation_language_id_question_id ON job_question_translation (language_id, question_id);


-- job_question_option
CREATE TABLE IF NOT EXISTS job_question_option (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    value TEXT NOT NULL,
    order_index INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_question_option_question FOREIGN KEY (question_id) REFERENCES job_question(id) ON DELETE CASCADE,
    CONSTRAINT uq_option_question UNIQUE (question_id, value),
    CONSTRAINT uq_option_order_index UNIQUE (question_id, order_index),
    CONSTRAINT chk_option_order_index
    CHECK (
        order_index > 0
    )
);

CREATE INDEX idx_option_question_id ON job_question_option (question_id);
CREATE INDEX idx_option_order_index ON job_question_option (order_index);


-- job_question_option_translation
CREATE TABLE IF NOT EXISTS job_question_option_translation (
    id BIGSERIAL PRIMARY KEY,
    option_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    label TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_question_option_translation_option FOREIGN KEY (option_id) REFERENCES job_question_option(id) ON DELETE CASCADE,
    CONSTRAINT fk_job_question_option_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT uq_option_language UNIQUE (option_id, language_id)
);

CREATE INDEX idx_option_translation_option_id ON job_question_option_translation (option_id);
CREATE INDEX idx_option_translation_language_id_option_id ON job_question_option_translation (language_id, option_id);


CREATE TABLE IF NOT EXISTS job_question_transition (
    id BIGSERIAL PRIMARY KEY,
    flow_id BIGINT NOT NULL,
    from_flow_question_id BIGINT NOT NULL,
    to_flow_question_id BIGINT NOT NULL,
    option_id BIGINT,
    condition_expression TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_question_transition_flow FOREIGN KEY (flow_id) REFERENCES job_request_flow(id) ON DELETE CASCADE,
    CONSTRAINT fk_job_question_transition_from_question FOREIGN KEY (from_flow_question_id) REFERENCES job_request_flow_question(id) ON DELETE CASCADE,
    CONSTRAINT fk_job_question_transition_to_question FOREIGN KEY (to_flow_question_id) REFERENCES job_request_flow_question(id) ON DELETE CASCADE,
    CONSTRAINT fk_job_question_transition_option FOREIGN KEY (option_id) REFERENCES job_question_option(id) ON DELETE SET NULL,
    CONSTRAINT uq_job_question_transition UNIQUE (flow_id, from_flow_question_id, option_id),
--    CONSTRAINT uq_job_question_transition_option UNIQUE (flow_id, from_flow_question_id, to_flow_question_id),
    CONSTRAINT check_job_question_from_not_equal_to CHECK (from_flow_question_id != to_flow_question_id);
);

CREATE INDEX idx_job_question_transition_flow ON job_question_transition(flow_id);
CREATE INDEX idx_job_question_transition_from_question ON job_question_transition(from_flow_question_id);
CREATE INDEX idx_job_question_transition_to_question ON job_question_transition(to_flow_question_id);
CREATE INDEX idx_job_question_transition_flow_id_from_question_id_option_id ON job_question_transition(flow_id, from_flow_question_id, option_id);



CREATE TYPE job_status_enum AS ENUM (
    'DRAFT', 'SUBMITTED', 'ACCEPTED', 'IN_PROGRESS', 'COMPLETED'
);

CREATE TYPE when_to_start_job_enum AS ENUM (
    'IMMEDIATELY',
    'WITHIN_2_DAYS',
    'WITHIN_4_DAYS',
    'WITHIN_A_WEEK',
    'WITHIN_2_WEEKS',
    'WITHIN_3_WEEKS',
    'WITHIN_A_MONTH',
    'WITHIN_2_MONTHS',
    'WITHIN_3_MONTHS',
    'WITHIN_6_MONTHS',
    'WITHIN_1_YEAR',
    'ANY_TIME',
    'AGREEMENT'
);

CREATE TABLE IF NOT EXISTS job_request (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    description TEXT,
    budget NUMERIC,
    when_to_start when_to_start_job_enum NOT NULL,
    status job_status_enum NOT NULL DEFAULT 'DRAFT',
    address_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_request_service FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE RESTRICT,
    CONSTRAINT fk_job_request_customer FOREIGN KEY (customer_id) REFERENCES user_profile(user_profile_id) ON DELETE RESTRICT,
    CONSTRAINT fk_job_request_address FOREIGN KEY (address_id) REFERENCES address(address_id) ON DELETE SET NULL
);

-- Indexes
CREATE INDEX idx_job_request_service ON job_request(service_id);
CREATE INDEX idx_job_request_customer ON job_request(customer_id);
CREATE INDEX idx_job_request_status ON job_request(status);
CREATE INDEX idx_job_request_service_id_status ON job_request(service_id, status);


CREATE TABLE IF NOT EXISTS job_question_answers (
    id BIGSERIAL PRIMARY KEY,
    job_request_id BIGINT NOT NULL, -- Foreign key to job_request
    flow_question_id BIGINT NOT NULL, -- Foreign key to job_request_flow_question
    answer_text TEXT,
    answer_number NUMERIC(10, 2),
    answer_boolean BOOLEAN,
    answer_date DATE,
    answer_option_ids BIGINT[],

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_answer_job FOREIGN KEY (job_request_id) REFERENCES job_request(id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_question FOREIGN KEY (flow_question_id) REFERENCES job_request_flow_question(id) ON DELETE CASCADE,

    -- CHECK: At least one value must be non-null
     CHECK (
        answer_text IS NOT NULL OR
        answer_number IS NOT NULL OR
        answer_boolean IS NOT NULL OR
        answer_date IS NOT NULL OR
        array_length(answer_option_ids, 1) IS NOT NULL
        )
);

CREATE INDEX idx_answers_job_request ON job_question_answers (job_request_id);
CREATE INDEX idx_answers_flow_question ON job_question_answers (flow_question_id);
CREATE INDEX idx_answers_job_flow_question ON job_question_answers (job_request_id, flow_question_id);

--==============================================================================
--SERVICE PROVIDER RELATED TABLES
--==============================================================================

CREATE TYPE service_provider_type_enum AS ENUM ('FREELANCER', 'SOLE_TRADER', 'LIMITED_COMPANY');
CREATE TYPE portfolio_type_enum AS ENUM ('WEBSITE', 'IMAGE', 'VIDEO', 'DOCUMENT', 'OTHER');
CREATE TYPE verification_status_enum AS ENUM ('PENDING', 'IN_REVIEW', 'APPROVED', 'REJECTED');

CREATE TABLE IF NOT EXISTS service_provider (
    id BIGSERIAL PRIMARY KEY,
    business_name VARCHAR(255) NOT NULL,
    business_description TEXT,
    service_provider_type service_provider_type_enum NOT NULL DEFAULT 'SOLE_TRADER',
    country_id BIGINT,
    business_address_id BIGINT,
    num_employees INT,
    max_travel_distance_in_km NUMERIC(4, 2),
    portfolio_url TEXT,
    portfolio_type portfolio_type_enum NOT NULL DEFAULT 'WEBSITE',
    business_logo_url TEXT,
    verification_status verification_status_enum NOT NULL DEFAULT 'PENDING',
    average_rating NUMERIC(2, 1) DEFAULT 0.0 CHECK (average_rating >= 0.0 AND average_rating <= 5.0),
    number_of_reviews INT DEFAULT 0,
    years_of_experience INT,
    is_verified BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    is_blocked BOOLEAN DEFAULT false,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_service_provider_business_address FOREIGN KEY (business_address_id) REFERENCES address(address_id) ON DELETE SET NULL,
    CONSTRAINT fk_service_provider_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE SET NULL,
    CONSTRAINT chk_number_of_employees CHECK (service_provider_type != 'FREELANCER' OR num_employees = 1),
);

CREATE INDEX idx_service_provider_business_address_id ON service_provider(business_address_id);

CREATE TABLE IF NOT EXISTS service_employee (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    is_blocked BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_service_employee_user FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id),
    CONSTRAINT fk_service_employee_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id)
);

CREATE INDEX idx_service_employee_provider ON service_employee(provider_id);
CREATE INDEX idx_service_employee_is_active ON service_employee(is_active);


-- Create role type for service provider roles - EMPLOYEE, MANAGER, OWNER, etc.
CREATE TABLE IF NOT EXISTS service_provider_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);


CREATE TABLE IF NOT EXISTS service_employee_role (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES service_provider_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES service_employee(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS service_provider_services (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    linked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sps_service FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE CASCADE,
    CONSTRAINT fk_sps_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
);

CREATE INDEX idx_sps_provider ON service_provider_services(provider_id);
CREATE INDEX idx_sps_service ON service_provider_services(service_id);
CREATE INDEX idx_sps_is_active ON service_provider_services(is_active);
CREATE INDEX idx_sps_service_id_is_active ON service_provider_services(service_id, is_active);



CREATE TABLE IF NOT EXISTS service_provider_verification_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    provider_type service_provider_type_enum NOT NULL DEFAULT 'SOLE_TRADER',
    is_mandatory BOOLEAN NOT NULL DEFAULT true,
    document_required BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS service_provider_verification (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    type_id BIGINT NOT NULL,
    verification_status verification_status_enum NOT NULL DEFAULT 'PENDING',
    document_url TEXT,
    reason_for_rejection TEXT,
    verification_note TEXT,
    verified_by BIGINT, -- User who verified the document
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_service_provider_verification_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id),
    CONSTRAINT fk_service_provider_verification_type FOREIGN KEY (type_id) REFERENCES service_provider_verification_type(id),
    CONSTRAINT fk_service_provider_verification_verified_by FOREIGN KEY (verified_by) REFERENCES user_profile(user_profile_id) ON DELETE SET NULL
);

CREATE INDEX idx_verification_provider ON service_provider_verification(provider_id);
CREATE INDEX idx_verification_status ON service_provider_verification(verification_status);


CREATE TABLE service_provider_tax_info (
    id BIGSERIAL PRIMARY KEY,
    service_provider_id BIGINT NOT NULL,
    tax_payer_id_number VARCHAR(50),
    is_vat_registered BOOLEAN NOT NULL DEFAULT FALSE,
    is_tax_exempt BOOLEAN NOT NULL DEFAULT FALSE,
    tax_exempt_certificate_number VARCHAR(50),
    tax_exemption_reason TEXT,
    income_tax_classification VARCHAR(50), -- e.g., 'Level 1', 'Level 2', etc. in Ethiopia
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_spti_service_provider FOREIGN KEY (service_provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
);

--===================================================================
-- AGREEMENT DOCUMENT RELATED TABLES
--===================================================================

CREATE TABLE legal_document (
    id BIGSERIAL PRIMARY KEY,
    country_id BIGINT, -- Foreign key to country table
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    version VARCHAR(20),
    is_required BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    effective_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_legal_document_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE SET NULL,
    CONSTRAINT unique_legal_document_type_country_id UNIQUE (name_internal, country_id)
);

CREATE INDEX idx_legal_document_country_id ON legal_document(country_id);
CREATE INDEX idx_legal_document_country_id_is_required_is_active ON legal_document(country_id, is_required, is_active);

CREATE TABLE legal_document_translation (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    document_url TEXT,
    content TEXT,
    language_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_adt_document FOREIGN KEY (document_id) REFERENCES legal_document(id) ON DELETE CASCADE,
    CONSTRAINT fk_adt_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT chk_url_or_content CHECK (
        (document_url IS NOT NULL) OR (content IS NOT NULL)
    )
);

CREATE INDEX idx_legal_document_translation_document_id ON legal_document_translation(document_id);


CREATE TABLE service_provider_agreement (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    is_signed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_spa_document FOREIGN KEY (document_id) REFERENCES legal_document(id) ON DELETE CASCADE,
    CONSTRAINT fk_spa_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
);

CREATE INDEX idx_spa_provider ON service_provider_agreement(provider_id);
CREATE INDEX idx_spa_document ON service_provider_agreement(document_id);
CREATE INDEX idx_spa_document_id_provider_id ON service_provider_agreement(document_id, provider_id);



























-- Create ENUM type for provider_type
CREATE TYPE service_provider_type_enum AS ENUM ('INDIVIDUAL', 'COMPANY');
CREATE TYPE portfolio_type_enum AS ENUM ('WEBSITE', 'IMAGE', 'VIDEO');
CREATE TYPE verification_status_enum AS ENUM ('PENDING', 'APPROVED', 'REJECTED');

-- Create ServiceProvider Table
CREATE TABLE IF NOT EXISTS service_provider (
    service_provider_id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    business_name VARCHAR(255) NOT NULL,
    business_description TEXT,
    service_provider_type service_provider_type_enum NOT NULL DEFAULT 'INDIVIDUAL',
    business_address_id BIGINT,  -- Foreign key to the address table
    max_travel_distance_in_km INT,
    num_employees INT,
    portfolio_url TEXT,
    portfolio_type portfolio_type_enum,
    business_logo_url TEXT,
    verification_status verification_status_enum NOT NULL DEFAULT 'PENDING',
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_service_provider_user_profile FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_service_provider_address FOREIGN KEY (business_address_id) REFERENCES address(address_id) ON DELETE SET NULL
);

-- Create Indexes
CREATE INDEX idx_service_provider_user_profile_id ON service_provider(user_profile_id);
CREATE INDEX idx_service_provider_business_address_id ON service_provider(business_address_id);
CREATE INDEX idx_service_provider_provider_type ON service_provider(provider_type);


-- Create ServiceCompanyEmployee Table
CREATE TABLE IF NOT EXISTS service_provider_employee (
    service_employee_id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,       -- Foreign key to the user_profile table
    service_provider_id BIGINT NOT NULL,    -- Foreign key to the service_company table
    CONSTRAINT fk_service_provider_employee_user_profile FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_service_provider_employee_service_provider FOREIGN KEY (service_provider_id) REFERENCES service_provider(service_provider_id) ON DELETE CASCADE
);

CREATE INDEX idx_service_provider_employee_service_provider_id ON service_provider_employee(service_provider_id);


-- Table: VerificationType
CREATE TABLE IF NOT EXISTS service_provider_verification_type (
    verification_type_id BIGSERIAL PRIMARY KEY,
    verification_name VARCHAR(100) NOT NULL,
    service_provider_type service_provider_type_enum NOT NULL DEFAULT 'INDIVIDUAL',
    is_mandatory BOOLEAN DEFAULT FALSE,
    document_must_be_attached BOOLEAN DEFAULT FALSE
);


----------------------- SERVICE RELATED TABLES ----------------------------
CREATE TABLE IF NOT EXISTS service_category (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE INDEX idx_service_category_name ON service_category(name);


CREATE TABLE IF NOT EXISTS service (
    service_id BIGSERIAL PRIMARY KEY,
    service_category_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

    CONSTRAINT fk_service_service_category FOREIGN KEY (service_category_id) REFERENCES service_category(category_id) ON DELETE SET NULL
);

CREATE INDEX idx_service_name ON service(name);
CREATE INDEX idx_service_category_id ON service(service_category_id);


----------------------- JOB QUESTION FLOW RELATED TABLES ------------------
CREATE TABLE IF NOT EXISTS job_question_flow (
    flow_id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    flow_name VARCHAR(255) NOT NULL,
    flow_description TEXT,
    version INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

    CONSTRAINT fk_job_question_flow_service FOREIGN KEY (service_id) REFERENCES service(service_id) ON DELETE CASCADE
);

CREATE INDEX idx_job_question_flow_service_id ON job_question_flow(service_id);


CREATE TYPE job_question_type_enum AS ENUM ('OPTION', 'TEXT', 'NUMBER', 'BOOLEAN');
CREATE TABLE IF NOT EXISTS job_question (
    question_id BIGSERIAL PRIMARY KEY,
    flow_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    type job_question_type_enum NOT NULL DEFAULT 'TEXT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_job_question_flow FOREIGN KEY (flow_id) REFERENCES job_question_flow(flow_id) ON DELETE CASCADE
);

CREATE INDEX idx_job_question_flow_id ON job_question(flow_id);

CREATE TABLE IF NOT EXISTS job_question_option (
    option_id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    label VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_question_option_question FOREIGN KEY (question_id) REFERENCES job_question(question_id) ON DELETE CASCADE
);

CREATE INDEX idx_job_question_option_question_id ON job_question_option(question_id);


CREATE TABLE IF NOT EXISTS job_question_transition (
    transition_id BIGSERIAL PRIMARY KEY,
    current_question_id BIGINT NOT NULL, -- The question that this transition starts from
    question_option_id BIGINT NOT NULL,  -- The option selected that triggers this transition
    next_question_id BIGINT NOT NULL, -- The question that this transition leads to

    CONSTRAINT fk_job_question_transition_current_question_id FOREIGN KEY (current_question_id) REFERENCES job_question(question_id) ON DELETE CASCADE,
    CONSTRAINT fk_job_question_transition_question_option_id FOREIGN KEY (question_option_id) REFERENCES job_question_option(option_id) ON DELETE CASCADE,
    CONSTRAINT fk_job_question_transition_next_question_id FOREIGN KEY (next_question_id) REFERENCES job_question(question_id)
);

CREATE INDEX idx_job_question_transition_current_question_id ON job_question_transition(current_question_id);
CREATE INDEX idx_job_question_transition_question_option_id ON job_question_transition(question_option_id);
CREATE INDEX idx_job_question_transition_next_question_id ON job_question_transition(next_question_id);

----------------------- SYSTEM CONFIGURATION TABLE ------------------------

CREATE TABLE IF NOT EXISTS system_config (
    config_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,  -- Unique key for config (e.g. "maintenance_mode")
    value TEXT NOT NULL,                -- Raw value stored as string
    type data_type_enum NOT NULL DEFAULT 'STRING', -- Type of the value (INTEGER, DOUBLE, BOOLEAN, STRING)
    created_by BIGINT NOT NULL, -- User who created the config
    updated_by BIGINT NOT NULL, -- User who last updated the config
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_system_config_user_profile_created_by FOREIGN KEY (created_by) REFERENCES user_profile(user_profile_id) ON DELETE SET NULL,
    CONSTRAINT fk_system_config_user_profile_updated_by FOREIGN KEY (updated_by) REFERENCES user_profile(user_profile_id) ON DELETE SET NULL
);

CREATE INDEX idx_system_config_created_by ON system_config(created_by);
CREATE INDEX idx_system_config_updated_by ON system_config(updated_by);

----------------------- JOB REQUEST RELATED TABLES ------------------------

