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


CREATE TABLE job_request_media (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL,
    media_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_jrm_request FOREIGN KEY (request_id) REFERENCES job_request(id) ON DELETE CASCADE
);

CREATE INDEX idx_jrm_request_id ON job_request_media(request_id);


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


--===================================================================
-- PAYMENT RELATED RELATED TABLES
--===================================================================
--CREATE TYPE calculation_method_enum AS ENUM ('PERCENTAGE', 'FIXED', 'PERCENTAGE_PLUS_FIXED');
CREATE TYPE payer_entity_type_enum AS ENUM ('SERVICE_PROVIDER', 'CUSTOMER');
CREATE TYPE product_type_enum AS ENUM ('SUBSCRIPTION', 'ADD_ON');
CREATE TYPE payment_intent_status_enum AS ENUM ('PENDING', 'PROCESSING', 'SUCCEEDED', 'FAILED', 'CANCELLED', 'EXPIRED');
CREATE TYPE transaction_status_enum AS ENUM ('INITIATED', 'PROCESSING', 'SUCCEEDED', 'FAILED', 'TIMEOUT', 'DISPUTED', 'REFUNDED');
CREATE TYPE invoice_status_enum AS ENUM ('ISSUED', 'PAID', 'CANCELLED');
CREATE TYPE billing_cycle_enum AS ENUM ('DAY', 'WEEK', 'MONTH', 'YEAR');
CREATE TYPE coupon_status_enum AS ENUM ('ACTIVE', 'INACTIVE', 'REDEEMED', 'EXPIRED');
CREATE type discount_user_type AS enum('NEW_USER', 'EXISTING_USER', 'ANY_USER');


-- TABLES
CREATE TABLE tax_rule (
    id BIGSERIAL PRIMARY KEY,
    country_id BIGINT,
    region_id BIGINT,
    tax_type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
--    tax_calculation_method calculation_method_enum NOT NULL,
    percentage_value NUMERIC(12,4) NOT NULL CHECK (percentage_value >= 0 AND percentage_value <= 100),
    fixed_value NUMERIC(12,4) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    effective_start_date TIMESTAMP NOT NULL,
    effective_end_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tax_rule_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON DELETE SET NULL,
    CONSTRAINT fk_tax_rule_region FOREIGN KEY (region_id) REFERENCES region(region_id) ON DELETE SET NULL
);

CREATE INDEX idx_tax_rule_country ON tax_rule(country_id);
CREATE INDEX idx_tax_rule_region ON tax_rule(region_id);


CREATE TABLE payment_method (
    id BIGSERIAL PRIMARY KEY,
    country_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_method_country FOREIGN KEY (country_id) REFERENCES country(country_id)
);

CREATE INDEX idx_payment_method_country ON payment_method(country_id);


-- TABLE: payment_method_translation
CREATE TABLE payment_method_translation (
    id BIGSERIAL PRIMARY KEY,
    payment_method_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pmt_payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_method(id) ON DELETE CASCADE,
    CONSTRAINT fk_pmt_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT uq_pmt_unique UNIQUE (payment_method_id, language_id)
);

CREATE INDEX idx_pmt_payment_method_id ON payment_method_translation(payment_method_id);
CREATE INDEX idx_pmt_language_id ON payment_method_translation(language_id);
CREATE INDEX idx_pmt_payment_method_id_language_id ON payment_method_translation(payment_method_id, language_id);

-- TABLE: payment_intent
CREATE TABLE payment_intent (
    id BIGSERIAL PRIMARY KEY,
    payer_entity_type payer_entity_type_enum NOT NULL,
    payer_entity_id BIGINT NOT NULL, -- service provider or customer. No referential integrity here
    amount_before_tax NUMERIC(15,4) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    product_type product_type_enum NOT NULL,
    status payment_intent_status_enum NOT NULL,
    payment_due_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payment_intent_payer ON payment_intent(payer_entity_type, payer_entity_id);
CREATE INDEX idx_payment_intent_payer_status ON payment_intent(payer_entity_type, payer_entity_id, status);


-- TABLE: transaction
CREATE TABLE transaction (
    id BIGSERIAL PRIMARY KEY,
    payment_intent_id BIGINT,
    payment_method_id BIGINT,
    sub_total_amount NUMERIC(15,4) NOT NULL,
    tax_amount NUMERIC(15,4) NOT NULL,
    amount_paid NUMERIC(15,4) NOT NULL,
    txn_reference VARCHAR(255) UNIQUE,
    currency VARCHAR(10) NOT NULL,
    description TEXT,
    status transaction_status_enum NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_transaction_payment_intent FOREIGN KEY (payment_intent_id) REFERENCES payment_intent(id) ON DELETE SET NULL,
    CONSTRAINT fk_transaction_payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_method(id) ON DELETE SET NULL
);

CREATE INDEX idx_transaction_payment_intent ON transaction(payment_intent_id);
CREATE INDEX idx_transaction_payment_intent_payment_method ON transaction(payment_intent_id, payment_method_id);
CREATE INDEX idx_transaction_status ON transaction(status);


CREATE TABLE service_provider_invoice (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    provider_tax_info_id BIGINT,
    transaction_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    region_id BIGINT,
    description TEXT,
    sub_total_amount NUMERIC(15,4) NOT NULL,
    tax_amount NUMERIC(15,4) NOT NULL,
    total_amount NUMERIC(15,4) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    status invoice_status_enum NOT NULL,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE, -- if the invoice is locked, it cannot be modified
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_invoice_transaction FOREIGN KEY (transaction_id) REFERENCES transaction(id),
    CONSTRAINT fk_invoice_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id),
    CONSTRAINT fk_invoice_provider_tax_info FOREIGN KEY (provider_tax_info_id) REFERENCES service_provider_tax_info(id),
    CONSTRAINT fk_invoice_country FOREIGN KEY (country_id) REFERENCES country(country_id),
    CONSTRAINT fk_invoice_region FOREIGN KEY (region_id) REFERENCES region(region_id)
);

CREATE INDEX idx_invoice_provider ON service_provider_invoice(provider_id);
CREATE INDEX idx_invoice_region ON service_provider_invoice(region_id);
CREATE INDEX idx_invoice_country ON service_provider_invoice(country_id);
CREATE INDEX idx_invoice_transaction ON service_provider_invoice(transaction_id);
CREATE INDEX idx_invoice_service_provider_status ON service_provider_invoice(provider_id, status);



CREATE TABLE service_provider_invoice_item (
    id BIGSERIAL PRIMARY KEY,
    parent_invoice_id BIGINT NOT NULL,
    tax_rule_id BIGINT NOT NULL,
    description TEXT,
    product_type product_type_enum NOT NULL,
    unit_price NUMERIC(15,4) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    sub_total_amount NUMERIC(15,4) NOT NULL,
    tax_amount NUMERIC(15,4) NOT NULL,
    total_amount NUMERIC(15,4) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_invoice_item_invoice FOREIGN KEY (parent_invoice_id) REFERENCES service_provider_invoice(id) ON DELETE CASCADE,
    CONSTRAINT fk_invoice_item_tax_rule FOREIGN KEY (tax_rule_id) REFERENCES tax_rule(id)
);

CREATE INDEX idx_invoice_item_invoice ON service_provider_invoice_item(parent_invoice_id);
CREATE INDEX idx_invoice_item_tax_rule ON service_provider_invoice_item(tax_rule_id);


CREATE TABLE add_on_plan (
    id BIGSERIAL PRIMARY KEY,
--    service_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    price_amount NUMERIC(15,4) NOT NULL CHECK(price_amount >= 0),
    credits_included INT NOT NULL DEFAULT 0,
    sort_order INT NOT NULL DEFAULT 0,
    expires_at TIMESTAMP,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_add_on_plan_service FOREIGN KEY (service_id) REFERENCES service(id),
    CONSTRAINT fk_add_on_plan_country FOREIGN KEY (country_id) REFERENCES country(country_id),
    CONSTRAINT uq_add_on_plan_service_plan_order UNIQUE (service_id, plan_order)
);

CREATE INDEX idx_addonplan_service_country ON add_on_plan(service_id, country_id);
CREATE INDEX idx_addonplan_service_country_is_active ON add_on_plan(service_id, country_id, is_active);


CREATE TABLE add_on_plan_translation (
    id BIGSERIAL PRIMARY KEY,
    add_on_plan_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_add_on_plan_translation FOREIGN KEY (add_on_plan_id) REFERENCES add_on_plan(id) ON DELETE CASCADE,
    CONSTRAINT fk_add_on_plan_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE
);

CREATE INDEX idx_add_on_plan_trans_add_on_plan ON add_on_plan_translation(add_on_plan_id);
CREATE INDEX idx_add_on_plan_trans_language ON add_on_plan_translation(language_id);

CREATE TABLE service_provider_add_on (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL,
--    payment_intent_id BIGINT NOT NULL,
    add_on_plan_id BIGINT NOT NULL,
    initial_credits DECIMAL(10,2) NOT NULL CHECK(initial_credits >= 0),
    used_credits DECIMAL(10,2) NOT NULL CHECK(used_credits >= 0),
    purchased_at TIMESTAMP NOT NULL,
--    upgraded_from BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    expires_at TIMESTAMP,
    CONSTRAINT fk_sp_add_on_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id),
    CONSTRAINT fk_sp_add_on_payment_intent FOREIGN KEY (payment_intent_id) REFERENCES payment_intent(id),
    CONSTRAINT fk_sp_add_on_plan FOREIGN KEY (add_on_plan_id) REFERENCES add_on_plan(id),
);

CREATE INDEX idx_sp_add_on_provider ON service_provider_add_on(provider_id);


CREATE TABLE add_on_transaction (
    id BIGSERIAL PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    service_provider_add_on_id BIGINT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_add_on_transaction_transaction FOREIGN KEY (transaction_id) REFERENCES transaction(id),
    CONSTRAINT fk_add_on_transaction_service_provider_add_on FOREIGN KEY (service_provider_add_on_id) REFERENCES service_provider_add_on(id)
);

CREATE INDEX idx_add_on_transaction_transaction ON add_on_transaction(transaction_id);
CREATE INDEX idx_add_on_transaction_service_provider_add_on ON add_on_transaction(service_provider_add_on_id);



CREATE TABLE subscription_plan (
    id BIGSERIAL PRIMARY KEY,
--    service_category_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price_amount NUMERIC(15,4) NOT NULL CHECK(price_amount > 0),
    billing_cycle billing_cycle_enum NOT NULL,
    billing_cycle_count INT NOT NULL DEFAULT 1 CHECK(billing_cycle_count > 0),
    trial_period_days INT NOT NULL DEFAULT 0 CHECK(trial_period_days >= 0),
    credits_included DECIMAL(10,2) NOT NULL DEFAULT 0,
    sort_order INT NOT NULL DEFAULT 0,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    slug VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscription_plan_country FOREIGN KEY (country_id) REFERENCES country(country_id),
    CONSTRAINT uq_subscription_plan_name_country UNIQUE (name, country_id)
);

CREATE INDEX idx_subscription_plan_slug ON subscription_plan(slug);


CREATE TABLE subscription_plan_translation (
    id BIGSERIAL PRIMARY KEY,
    subscription_plan_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subscription_plan_translation FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plan(id) ON DELETE CASCADE,
    CONSTRAINT fk_subscription_plan_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT uq_subscription_plan_translation_unique UNIQUE (subscription_plan_id, language_id)
);

CREATE INDEX idx_subscription_plan_trans_subscription_plan ON subscription_plan_translation(subscription_plan_id);
CREATE INDEX idx_subscription_plan_trans_language ON subscription_plan_translation(language_id);


CREATE TABLE service_provider_subscription (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    subscription_plan_id BIGINT NOT NULL,
--    payment_intent_id BIGINT NOT NULL,
    initial_credits DECIMAL(10,2) NOT NULL CHECK (initial_credits >= 0),
    used_credits DECIMAL(10,2) NOT NULL CHECK (used_credits >= 0),
    subscribed_at TIMESTAMP NOT NULL,
--    valid_until TIMESTAMP,
    upgraded_from BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    expires_at TIMESTAMP,
    CONSTRAINT fk_sp_sub_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id),
    CONSTRAINT fk_sp_sub_subscription_plan FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plan(id),
    CONSTRAINT fk_sp_sub_payment_intent FOREIGN KEY (payment_intent_id) REFERENCES payment_intent(id),
    CONSTRAINT fk_sp_sub_upgraded_from FOREIGN KEY (upgraded_from) REFERENCES service_provider_subscription(id)

);
CREATE UNIQUE INDEX ux_one_active_subscription_per_provider
ON service_provider_subscription(provider_id)
WHERE is_active = true; -- Only one active subscription per provider
CREATE INDEX idx_sp_sub_provider ON service_provider_subscription(provider_id);


CREATE TABLE subscription_transaction (
    id BIGSERIAL PRIMARY KEY,
    service_provider_subscription_id BIGINT NOT NULL,
    transaction_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscription_transaction_subscription
        FOREIGN KEY (service_provider_subscription_id)
        REFERENCES service_provider_subscription(id),

    CONSTRAINT fk_subscription_transaction_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES transaction(id)
);

CREATE INDEX idx_subscription_transaction_subscription
    ON subscription_transaction(service_provider_subscription_id);

CREATE INDEX idx_subscription_transaction_transaction
    ON subscription_transaction(transaction_id);



CREATE TABLE add_on_usage (
    id BIGSERIAL PRIMARY KEY,
    service_provider_addon_id BIGINT NOT NULL,
    service_provider_lead_id BIGINT NOT NULL,
    used_credit_amount DECIMAL(10,2) NOT NULL CHECK (used_credit_amount > 0),
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_addon_usage_service_provider_addon FOREIGN KEY (service_provider_addon_id) REFERENCES service_provider_add_on(id),
    CONSTRAINT fk_addon_usage_service_provider_lead FOREIGN KEY (service_provider_lead_id) REFERENCES service_provider_lead(id)
);

CREATE INDEX idx_addon_usage_service_provider_addon ON add_on_usage(service_provider_addon_id);
CREATE INDEX idx_addon_usage_service_provider_lead ON add_on_usage(service_provider_lead_id);



CREATE TABLE discount_plan (
    id BIGSERIAL PRIMARY KEY,
    country_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
--    discount_calculation_method calculation_method_enum NOT NULL DEFAULT 'PERCENTAGE',
    fixed_value NUMERIC(15,4) NOT NULL,
    percentage_value NUMERIC(15,4) NOT NULL check (percentage_value >= 0 AND percentage_value <= 100),
    starts_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    usage_limit INT NOT NULL DEFAULT 0,
    per_user_limit INT NOT NULL DEFAULT 0,
    total_use_count INT NOT NULL DEFAULT 0,
    max_discount_value NUMERIC(15,4),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    coupon_required BOOLEAN NOT NULL DEFAULT TRUE,
    applies_to discount_user_type NOT NULL DEFAULT 'NEW_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_discount_plan_country FOREIGN KEY (country_id) REFERENCES country(country_id)
);

CREATE INDEX idx_discount_plan_country ON discount_plan(country_id);


CREATE TABLE discount_plan_translation (
    id BIGSERIAL PRIMARY KEY,
    discount_plan_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_discount_plan_translation_discount FOREIGN KEY (discount_id) REFERENCES discount_plan(id) ON DELETE CASCADE,
    CONSTRAINT fk_discount_plan_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT uq_discount_trans_unique UNIQUE (discount_id, language_id)
);

CREATE INDEX idx_discount_trans_discount ON discount_plan_translation(discount_id);
CREATE INDEX idx_discount_trans_language ON discount_plan_translation(language_id);
CREATE INDEX idx_discount_trans_unique ON discount_plan_translation(discount_id, language_id);


CREATE TABLE coupon (
    id BIGSERIAL PRIMARY KEY,
    discount_plan_id BIGINT NOT NULL,
    code VARCHAR(100) UNIQUE NOT NULL,
    usage_limit INT NOT NULL DEFAULT 0,
    per_user_limit INT NOT NULL DEFAULT 0,
    total_use_count INT NOT NULL DEFAULT 0,
    starts_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    status coupon_status_enum NOT NULL,
    created_by BIGINT,
    is_global BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_coupon_discount_plan FOREIGN KEY (discount_plan_id) REFERENCES discount_plan(id),
    CONSTRAINT fk_coupon_created_by FOREIGN KEY (created_by) REFERENCES user_profile(user_profile_id)
);

CREATE INDEX idx_coupon_discount_plan ON coupon(discount_plan_id);
CREATE INDEX idx_coupon_code ON coupon(code);
CREATE UNIQUE INDEX unique_global_coupon_per_discount ON coupon (is_global, discount_plan_id) WHERE is_global = true;


CREATE TABLE service_provider_discount_usage_tracker (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    discount_id BIGINT NOT NULL,
    usage_limit INT NOT NULL,
    use_count INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    first_used_at TIMESTAMP,
    last_used_at TIMESTAMP,
    CONSTRAINT fk_spdut_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id),
    CONSTRAINT fk_spdut_discount FOREIGN KEY (discount_id) REFERENCES discount_plan(id)
);

CREATE INDEX idx_spdut_provider ON service_provider_discount_usage_tracker(provider_id);
CREATE INDEX idx_spdut_discount ON service_provider_discount_usage_tracker(discount_id);
CREATE INDEX idx_spdut_provider_id_discount_id ON service_provider_discount_usage_tracker(provider_id, discount_id);


CREATE TABLE service_provider_coupon_tracker (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    usage_limit INT NOT NULL,
    use_count INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_spct_coupon FOREIGN KEY (coupon_id) REFERENCES coupon(id),
    CONSTRAINT fk_spct_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id)
);
CREATE INDEX idx_spct_coupon_is_active ON service_provider_coupon_tracker(coupon_id, is_active);
CREATE INDEX idx_spct_provider_is_active ON service_provider_coupon_tracker(provider_id, is_active);
CREATE INDEX idx_spct_provider_id_coupon_id_is_active ON service_provider_coupon_tracker(provider_id, coupon_id, is_active);


CREATE TABLE subscription_plan_discount_eligibility (
    id BIGSERIAL PRIMARY KEY,
    subscription_plan_id BIGINT NOT NULL,
    discount_plan_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dpspe_subscription_plan FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plan(id) ON DELETE CASCADE,
    CONSTRAINT fk_dpspe_discount_plan FOREIGN KEY (discount_plan_id) REFERENCES discount_plan(id) ON DELETE CASCADE,
    CONSTRAINT uq_dpspe_subscription_plan_id_discount_plan_id UNIQUE (subscription_plan_id, discount_plan_id)
);

CREATE INDEX idx_dpspe_subscription_plan ON subscription_plan_discount_eligibility(subscription_plan_id);
CREATE INDEX idx_dpspe_discount_plan ON subscription_plan_discount_eligibility(discount_plan_id);
CREATE INDEX idx_dpspe_is_active ON subscription_plan_discount_eligibility(is_active);

CREATE UNIQUE INDEX idx_dpspe_subscription_plan_id_is_active
ON subscription_plan_discount_eligibility(subscription_plan_id, is_active)
WHERE is_active = true;

CREATE INDEX idx_dpspe_subscription_plan_id_discount_plan_id
    ON subscription_plan_discount_eligibility(subscription_plan_id, discount_plan_id);

CREATE TABLE add_on_plan_discount_eligibility (
    id BIGSERIAL PRIMARY KEY,
    add_on_plan_id BIGINT NOT NULL,
    discount_plan_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dpae_add_on_plan FOREIGN KEY (add_on_plan_id) REFERENCES add_on_plan(id) ON DELETE CASCADE,
    CONSTRAINT fk_dpae_discount_plan FOREIGN KEY (discount_plan_id) REFERENCES discount_plan(id) ON DELETE CASCADE,
    CONSTRAINT uq_dpae_add_on_plan_id_discount_plan_id UNIQUE (add_on_plan_id, discount_plan_id)
);

CREATE INDEX idx_dpae_add_on_plan ON addon_plan_discount_eligibility(add_on_plan_id);
CREATE INDEX idx_dpae_discount_plan ON addon_plan_discount_eligibility(discount_plan_id);
CREATE INDEX idx_dpae_is_active ON addon_plan_discount_eligibility(is_active);

CREATE UNIQUE INDEX idx_dpae_add_on_plan_id_is_active
ON addon_plan_discount_eligibility(add_on_plan_id, is_active)
WHERE is_active = true;

CREATE INDEX idx_dpae_add_on_plan_id_discount_plan_id
    ON addon_plan_discount_eligibility(add_on_plan_id, discount_plan_id);


-- ==========================================================================================
-- END OF DISCOUNT PLANS
-- ==========================================================================================

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


-- ENUMS
CREATE TYPE job_lead_status_enum AS ENUM ('OPEN', 'IN_NEGOTIATION', 'ASSIGNED', 'CLOSED');
CREATE TYPE service_provider_lead_status_enum AS ENUM ('PENDING', 'VIEWED', 'QUOTED', 'DECLINED', 'EXPIRED', 'CUSTOMER_ACCEPTED', 'CUSTOMER_REJECTED', 'WITHDRAWN', 'FOLLOW_UP');
CREATE TYPE service_provider_quote_status_enum AS ENUM ('PENDING', 'SENT', 'VIEWED', 'ACCEPTED', 'REJECTED', 'EXPIRED');
CREATE TYPE notification_recipient_type_enum AS ENUM ('CUSTOMER', 'EMPLOYEE', 'OWNER', 'MANAGER', 'ALL');
CREATE TYPE message_context_type_enum AS ENUM ('JOB', 'SUPPORT', 'QUOTATION', 'GENERAL');

-- Skill
CREATE TABLE skill (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    service_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_skill_service FOREIGN KEY (service_id) REFERENCES service(id)
);
CREATE INDEX idx_skill_service_id ON skill(service_id);
CREATE INDEX idx_skill_name ON skill(name);

-- SkillTranslation
CREATE TABLE skill_translation (
    id BIGSERIAL PRIMARY KEY,
    skill_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_skill_translation_skill FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE,
    CONSTRAINT fk_skill_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id) ON DELETE CASCADE,
    CONSTRAINT uq_skill_translation_skill_id_language_id UNIQUE (skill_id, language_id)
);
CREATE INDEX idx_skill_translation_skill_id ON skill_translation(skill_id);
CREATE INDEX idx_skill_translation_language_id ON skill_translation(language_id);
CREATE INDEX idx_skill_skill_id_language_id ON skill_translation(skill_id, language_id);

-- ServiceProviderSkill
CREATE TABLE service_provider_skill (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sps_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id) ON DELETE CASCADE,
    CONSTRAINT fk_sps_skill FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE,
    CONSTRAINT uq_sps_provider_id_skill_id UNIQUE (provider_id, skill_id)
);
CREATE INDEX idx_sps_provider_id ON service_provider_skill(provider_id);
CREATE INDEX idx_sps_skill_id ON service_provider_skill(skill_id);

-- JobLead
CREATE TABLE job_lead (
    id BIGSERIAL PRIMARY KEY,
    job_request_id BIGINT,
    status job_lead_status_enum NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_job_lead_job_request FOREIGN KEY (job_request_id) REFERENCES job_request(id) ON DELETE SET NULL
);
CREATE INDEX idx_job_lead_job_request_id ON job_lead(job_request_id);
CREATE INDEX idx_job_lead_status ON job_lead(status);

-- ServiceProviderLead
CREATE TABLE service_provider_lead (
    id BIGSERIAL PRIMARY KEY,
    job_lead_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    status service_provider_lead_status_enum NOT NULL,
    response_duration BIGINT, -- in seconds
    viewed_at TIMESTAMP,
    responded_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_spl_job_lead FOREIGN KEY (job_lead_id) REFERENCES job_lead(id) ON DELETE CASCADE,
    CONSTRAINT fk_spl_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
);
CREATE INDEX idx_spl_job_lead_id ON service_provider_lead(job_lead_id);
CREATE INDEX idx_spl_provider_id ON service_provider_lead(provider_id);
CREATE INDEX idx_spl_job_lead_id_provider_id_status ON service_provider_lead(job_lead_id, provider_id, status);
CREATE INDEX idx_spl_status ON service_provider_lead(status);

-- ServiceProviderQuote
CREATE TABLE service_provider_quote (
    id BIGSERIAL PRIMARY KEY,
    provider_lead_id BIGINT NOT NULL UNIQUE,
    proposed_price NUMERIC(15,4),
    proposed_start_date TIMESTAMP,
    message TEXT NOT NULL,
    status service_provider_quote_status_enum NOT NULL,
    accepted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_spq_provider_lead FOREIGN KEY (provider_lead_id) REFERENCES service_provider_lead(id) ON DELETE CASCADE

);
CREATE INDEX idx_spq_provider_lead_id ON service_provider_quote(provider_lead_id);
CREATE INDEX idx_spq_status ON service_provider_quote(status);


-- Review
CREATE TABLE review (
    id BIGSERIAL PRIMARY KEY,
    job_request_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    service_provider_id BIGINT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_job_request FOREIGN KEY (job_request_id) REFERENCES job_request(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_customer FOREIGN KEY (customer_id) REFERENCES user_profile(user_profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_review_service_provider FOREIGN KEY (service_provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
);
CREATE INDEX idx_review_job_request_id ON review(job_request_id);
CREATE INDEX idx_review_customer_id ON review(customer_id);
CREATE INDEX idx_review_service_provider_id ON review(service_provider_id);

-- MessageThread
CREATE TABLE message_thread (
    id BIGSERIAL PRIMARY KEY,
    subject TEXT NOT NULL,
    context_type message_context_type_enum NOT NULL,
    context_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_mt_context_type_id ON message_thread(context_type, context_id);


-- MessageParticipant
CREATE TABLE message_participant (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    thread_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_mp_user_profile_id_thread_id UNIQUE (user_profile_id, thread_id),
    CONSTRAINT fk_mp_user_profile FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_mp_thread FOREIGN KEY (thread_id) REFERENCES message_thread(id) ON DELETE CASCADE
);
CREATE INDEX idx_mp_user_profile_id ON message_participant(user_profile_id);
CREATE INDEX idx_mp_thread_id ON message_participant(thread_id);

-- Message
CREATE TABLE message (
    id BIGSERIAL PRIMARY KEY,
    thread_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    reply_to BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_message_thread FOREIGN KEY (thread_id) REFERENCES message_thread(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES user_profile(user_profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_message_reply_to FOREIGN KEY (reply_to) REFERENCES message(id)
);
CREATE INDEX idx_message_thread_id ON message(thread_id);
CREATE INDEX idx_message_sender_id ON message(sender_id);

-- MessageRead
CREATE TABLE message_participant_status (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT NOT NULL REFERENCES message(id) ON DELETE CASCADE,
    participant_id BIGINT NOT NULL REFERENCES message_participant(id) ON DELETE CASCADE,
    read_at TIMESTAMP,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_mr_message_id_participant_id UNIQUE (message_id, participant_id),
    CONSTRAINT fk_mr_message FOREIGN KEY (message_id) REFERENCES message(id) ON DELETE CASCADE,
    CONSTRAINT fk_mr_participant FOREIGN KEY (participant_id) REFERENCES message_participant(id) ON DELETE CASCADE
);
CREATE INDEX idx_mr_message_id ON message_participant_status(message_id);
CREATE INDEX idx_mr_participant_id ON message_participant_status(participant_id);


-- MessageAttachment
CREATE TABLE message_attachment (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT NOT NULL,
    attachment_url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ma_message FOREIGN KEY (message_id) REFERENCES message(id) ON DELETE CASCADE
);
CREATE INDEX idx_ma_message_id ON message_attachment(message_id);


-- NotificationType
CREATE TABLE notification_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    recipient_type notification_recipient_type_enum NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_nt_recipient_type ON notification_type(recipient_type);
CREATE INDEX idx_nt_name ON notification_type(name);



-- NotificationTypeTranslation
CREATE TABLE notification_type_translation (
    id BIGSERIAL PRIMARY KEY,
    notification_type_id BIGINT NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    description TEXT,
    language_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ntt_notification_type FOREIGN KEY (notification_type_id) REFERENCES notification_type(id) ON DELETE CASCADE,
    CONSTRAINT fk_ntt_language FOREIGN KEY (language_id) REFERENCES language(language_id),
    CONSTRAINT uq_ntt_notification_type_id_language_id UNIQUE (notification_type_id, language_id)
);
CREATE INDEX idx_ntt_notification_type_id ON notification_type_translation(notification_type_id);
CREATE INDEX idx_ntt_language_id ON notification_type_translation(language_id);
CREATE INDEX idx_ntt_notification_type_id_language_id ON notification_type_translation(notification_type_id, language_id);


-- Notification
CREATE TABLE notification (
    id BIGSERIAL PRIMARY KEY,
    notification_type_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    read_at TIMESTAMP,
    redirect_url TEXT,
    is_broadcast BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_type FOREIGN KEY (notification_type_id) REFERENCES notification_type(id)
);
CREATE INDEX idx_notification_type_id ON notification(notification_type_id);


-- NotificationRecipient
CREATE TABLE notification_recipient (
    id BIGSERIAL PRIMARY KEY,
    recipient_id BIGINT NOT NULL,
    notification_id BIGINT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_nr_recipient_id_notification_id UNIQUE (recipient_id, notification_id),
    CONSTRAINT fk_nr_recipient FOREIGN KEY (recipient_id) REFERENCES user_profile(user_profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_nr_notification FOREIGN KEY (notification_id) REFERENCES notification(id)
);
CREATE INDEX idx_nr_recipient_id ON notification_recipient(recipient_id);
CREATE INDEX idx_nr_notification_id ON notification_recipient(notification_id);


-- NotificationPreference
CREATE TABLE notification_preference (
    id BIGSERIAL PRIMARY KEY,
    notification_type_id BIGINT NOT NULL,
    recipient_id BIGINT NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_np_notification_type_id_recipient_id UNIQUE (notification_type_id, recipient_id),
    CONSTRAINT fk_np_notification_type FOREIGN KEY (notification_type_id) REFERENCES notification_type(id),
    CONSTRAINT fk_np_recipient FOREIGN KEY (recipient_id) REFERENCES user_profile(user_profile_id)
);
CREATE INDEX idx_np_notification_type_id ON notification_preference(notification_type_id);
CREATE INDEX idx_np_recipient_id ON notification_preference(recipient_id);

--=================================================================================
-- SYSTEM
-- ================================================================================

-- Thumbnail table
CREATE TABLE thumbnail (
    id BIGSERIAL PRIMARY KEY,
    image_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Banner table
CREATE TABLE banner (
    id BIGSERIAL PRIMARY KEY,
    image_url TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--=================================================================================
-- SUPPORT TICKET
-- ================================================================================
CREATE TYPE ticket_status AS ENUM ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED');
CREATE TYPE ticket_type AS ENUM (
    'TECHNICAL_ISSUE',
    'BILLING',
    'ACCOUNT',
    'GENERAL_QUERY',
    'BUG_REPORT',
    'FEATURE_REQUEST',
    'OTHER'
);

CREATE TYPE report_reason AS ENUM (
    'PROVIDER_NOT_SHOWED_UP',
    'PAYMENT_ISSUE',
    'POOR_QUALITY_SERVICE',
    'MISBEHAVIOR_OR_RUDE_BEHAVIOR',
    'SERVICE_NOT_AS_DESCRIBED',
    'DAMAGE_TO_PROPERTY',
    'DELAYED_SERVICE',
    'UNAUTHORIZED_CHARGES',
    'SPAM_OR_FRAUD',
    'SAFETY_CONCERNS',
    'CANCELLATION_ISSUE',
    'OTHER'
);

CREATE TYPE ticket_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'URGENT');
CREATE TYPE event_mode AS ENUM ('ONLINE', 'OFFLINE');
CREATE TYPE severity_level AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');



CREATE TABLE support_ticket (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    subject TEXT NOT NULL,
    description TEXT,
    assigned_staff_id BIGINT,
    status ticket_status NOT NULL DEFAULT 'OPEN',
    ticket_type ticket_type NOT NULL,
    priority ticket_priority NOT NULL DEFAULT 'MEDIUM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    resolved_at TIMESTAMP,
    last_updated_by BIGINT,

    CONSTRAINT fk_support_ticket_user FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id),
    CONSTRAINT fk_support_ticket_staff FOREIGN KEY (assigned_staff_id) REFERENCES user_profile(user_profile_id),
    CONSTRAINT fk_support_ticket_updated_by FOREIGN KEY (last_updated_by) REFERENCES user_profile(user_profile_id)
);

CREATE INDEX idx_support_ticket_user ON support_ticket(user_profile_id);
CREATE INDEX idx_support_ticket_staff ON support_ticket(assigned_staff_id);



CREATE TABLE support_ticket_note (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    note TEXT NOT NULL,
    added_by BIGINT NOT NULL,
    is_internal BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_support_note_ticket FOREIGN KEY (ticket_id) REFERENCES support_ticket(id) ON DELETE CASCADE,
    CONSTRAINT fk_support_note_added_by FOREIGN KEY (added_by) REFERENCES user_profile(user_profile_id)
);

CREATE INDEX idx_support_note_ticket ON support_ticket_note(ticket_id);
CREATE INDEX idx_support_note_added_by ON support_ticket_note(added_by);



CREATE TABLE support_ticket_attachment (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    file_url TEXT NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_support_attachment_ticket FOREIGN KEY (ticket_id) REFERENCES support_ticket(id) ON DELETE CASCADE
);

CREATE INDEX idx_support_attachment_ticket ON support_ticket_attachment(ticket_id);


CREATE TABLE report (
    id BIGSERIAL PRIMARY KEY,
    reported_by BIGINT NOT NULL,
    reason report_reason NOT NULL,
    other_reason TEXT,
    reportee_name VARCHAR(255) NOT NULL,
    message TEXT,
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_report_reported_by FOREIGN KEY (reported_by) REFERENCES user_profile(user_profile_id)
);

CREATE INDEX idx_report_reported_by ON report(reported_by);


CREATE TABLE event (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    event_time TIMESTAMP NOT NULL,
    event_location_id BIGINT,
    event_mode event_mode NOT NULL,
    banner_url TEXT,
    thumbnail_url TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_event_event_location FOREIGN KEY (event_location_id) REFERENCES address(address_id)
);

CREATE INDEX idx_event_event_time ON event(event_time);
CREATE INDEX idx_event_is_active ON event(is_active);


CREATE TABLE event_translation (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_title TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_event_translation_event FOREIGN KEY (event_id) REFERENCES event(id),
    CONSTRAINT fk_event_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id),
    CONSTRAINT uq_event_translation UNIQUE (event_id, language_id)
);

CREATE INDEX idx_event_translation_event ON event_translation(event_id);
CREATE INDEX idx_event_translation_language ON event_translation(language_id);
CREATE INDEX idx_event_translation_event_language ON event_translation(event_id, language_id);


CREATE TABLE error_log (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT,
    country_id BIGINT,
    description TEXT NOT NULL,
    severity severity_level NOT NULL,
    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_error_log_user FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id),
    CONSTRAINT fk_error_log_country FOREIGN KEY (country_id) REFERENCES country(country_id)
);

CREATE INDEX idx_error_log_user ON error_log(user_profile_id);
CREATE INDEX idx_error_log_severity ON error_log(severity);


CREATE TABLE feedback (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT,
    message TEXT NOT NULL,
    response TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_feedback_user FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id)
);

CREATE INDEX idx_feedback_user ON feedback(user_profile_id);


CREATE TABLE faq_category (
    id BIGSERIAL PRIMARY KEY,
    country_id BIGINT,
    name TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_faq_category_country FOREIGN KEY (country_id) REFERENCES country(country_id)
);

CREATE INDEX idx_faq_category_country ON faq_category(country_id);


CREATE TABLE faq_category_translation (
    id BIGSERIAL PRIMARY KEY,
    faq_category_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_name TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_faq_category_translation_category FOREIGN KEY (faq_category_id) REFERENCES faq_category(id) ON DELETE CASCADE,
    CONSTRAINT fk_faq_category_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id),
    CONSTRAINT uq_faq_category_language UNIQUE (faq_category_id, language_id)
);

CREATE INDEX idx_faq_category_translation_category ON faq_category_translation(faq_category_id);
CREATE INDEX idx_faq_category_translation_language ON faq_category_translation(language_id);
CREATE INDEX idx_faq_category_translation_category_language ON faq_category_translation(faq_category_id, language_id);


CREATE TABLE faq (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_faq_category FOREIGN KEY (category_id) REFERENCES faq_category(id) ON DELETE CASCADE
);
CREATE INDEX idx_faq_category ON faq(category_id);


CREATE TABLE faq_translation (
    id BIGSERIAL PRIMARY KEY,
    faq_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_faq_translation_faq FOREIGN KEY (faq_id) REFERENCES faq(id) ON DELETE CASCADE,
    CONSTRAINT fk_faq_translation_language FOREIGN KEY (language_id) REFERENCES language(language_id),
    CONSTRAINT uq_faq_translation UNIQUE (faq_id, language_id)
);

CREATE INDEX idx_faq_translation_faq ON faq_translation(faq_id);
CREATE INDEX idx_faq_translation_language ON faq_translation(language_id);
CREATE INDEX idx_faq_translation_faq_language ON faq_translation(faq_id, language_id);

--=================================================================================
-- REFERRAL SYSTEM
-- ================================================================================
CREATE TYPE referral_participation_status AS ENUM ('OPEN', 'COMPLETED', 'EXPIRED');
CREATE TYPE referee_action_status AS ENUM ('PENDING', 'COMPLETED', 'EXPIRED');

CREATE TABLE referral_plan (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    referrer_discount_plan_id BIGINT,
    referee_discount_plan_id BIGINT,
    number_of_referrals_required INTEGER NOT NULL CHECK (number_of_referrals_required > 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    starts_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_referral_plan_referrer_discount_plan FOREIGN KEY (referrer_discount_plan_id) REFERENCES discount_plan(id),
    CONSTRAINT fk_referral_plan_referee_discount_plan FOREIGN KEY (referee_discount_plan_id) REFERENCES discount_plan(id)
);

CREATE INDEX idx_referral_plan_is_active ON referral_plan(is_active);
CREATE INDEX idx_referral_plan_name ON referral_plan(name);


CREATE TABLE referral_participation (
    id BIGSERIAL PRIMARY KEY,
    referrer_id BIGINT NOT NULL,
    referral_plan_id BIGINT NOT NULL,
    status referral_participation_status NOT NULL DEFAULT 'OPEN',
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_referral_participation_referrer FOREIGN KEY (referrer_id) REFERENCES service_provider(id),
    CONSTRAINT fk_referral_participation_plan FOREIGN KEY (referral_plan_id) REFERENCES referral_plan(id)
);

CREATE INDEX idx_referral_participation_referrer ON referral_participation(referrer_id);
CREATE INDEX idx_referral_participation_plan ON referral_participation(referral_plan_id);


CREATE TABLE referral_event (
    id BIGSERIAL PRIMARY KEY,
    participation_id BIGINT NOT NULL,
    referee_id BIGINT NOT NULL,
    referee_action_status referee_action_status NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_referral_event_participation FOREIGN KEY (participation_id) REFERENCES referral_participation(id) ON DELETE CASCADE,
    CONSTRAINT fk_referral_event_referee FOREIGN KEY (referee_id) REFERENCES service_provider(id),
    CONSTRAINT uq_referral_event_unique_referee UNIQUE (participation_id, referee_id)
);

CREATE INDEX idx_referral_event_participation ON referral_event(participation_id);
CREATE INDEX idx_referral_event_referee ON referral_event(referee_id);


CREATE TABLE referral_reward (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    participation_id BIGINT NOT NULL,
    issued_manually BOOLEAN NOT NULL DEFAULT FALSE,
    coupon_id BIGINT NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_referral_reward_provider FOREIGN KEY (provider_id) REFERENCES service_provider(id),
    CONSTRAINT fk_referral_reward_participation FOREIGN KEY (participation_id) REFERENCES referral_participation(id),
    CONSTRAINT fk_referral_reward_coupon FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

CREATE INDEX idx_referral_reward_provider ON referral_reward(provider_id);
CREATE INDEX idx_referral_reward_participation ON referral_reward(participation_id);
CREATE INDEX idx_referral_reward_coupon ON referral_reward(coupon_id);

--=================================================================================
-- ADVICE SYSTEM
-- ================================================================================
CREATE TYPE user_type AS ENUM ('CUSTOMER', 'SERVICE_PROVIDER');

CREATE TABLE advice_category (
    id BIGSERIAL PRIMARY KEY,
    parent_category_id BIGINT,
    user_type user_type NOT NULL,
    name TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_advice_category_parent FOREIGN KEY (parent_category_id) REFERENCES advice_category(id)
);

CREATE INDEX idx_advice_category_user_type ON advice_category(user_type);


CREATE TABLE advice_category_translation (
    id BIGSERIAL PRIMARY KEY,
    advice_category_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_name TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_advice_category_translation FOREIGN KEY (advice_category_id) REFERENCES advice_category(id),
    CONSTRAINT fk_advice_category_language FOREIGN KEY (language_id) REFERENCES language(id),
    CONSTRAINT uq_advice_category_translation UNIQUE (advice_category_id, language_id)
);

CREATE INDEX idx_advice_category_translation ON advice_category_translation(advice_category_id);


CREATE TABLE advice (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    created_by BIGINT NOT NULL,
    num_of_likes INTEGER DEFAULT 0,
    num_of_reads INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_advice_created_by FOREIGN KEY (created_by) REFERENCES user_profile(user_profile_id)
);

CREATE INDEX idx_advice_created_by ON advice(created_by);


CREATE TABLE advice_translation (
    id BIGSERIAL PRIMARY KEY,
    advice_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    display_title TEXT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_advice_translation_advice FOREIGN KEY (advice_id) REFERENCES advice(id),
    CONSTRAINT fk_advice_translation_language FOREIGN KEY (language_id) REFERENCES language(id),
    CONSTRAINT uq_advice_translation UNIQUE (advice_id, language_id)
);

CREATE INDEX idx_advice_translation_advice ON advice_translation(advice_id);
CREATE INDEX idx_advice_translation_advice_language ON advice_translation(advise_id, language_id);


CREATE TABLE advice_comment (
    id BIGSERIAL PRIMARY KEY,
    advice_id BIGINT NOT NULL,
    user_name TEXT,
    email TEXT,
    comment TEXT NOT NULL,
    has_liked BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_advice_comment_advice FOREIGN KEY (advice_id) REFERENCES advice(id) ON DELETE CASCADE
);

CREATE INDEX idx_advice_comment_advice ON advice_comment(advice_id);
