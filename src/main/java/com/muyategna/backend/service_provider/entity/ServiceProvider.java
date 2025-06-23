package com.muyategna.backend.service_provider.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.service_provider.enums.PortfolioType;
import com.muyategna.backend.service_provider.enums.ServiceProviderType;
import com.muyategna.backend.service_provider.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false, length = 255)
    private String businessName;

    @Column(name = "business_description", columnDefinition = "TEXT")
    private String businessDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_provider_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ServiceProviderType serviceProviderType = ServiceProviderType.SOLE_TRADER;

    @OneToMany(mappedBy = "provider")
    @ToString.Exclude
    @JsonIgnore
    @Transient
    private List<ServiceProviderService> subscribedServices = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "business_address_id")
    private Address businessAddress;

    @Column(name = "num_employees")
    private Integer numEmployees;

    @Column(name = "max_travel_distance_in_km", precision = 4, scale = 2)
    private BigDecimal maxTravelDistanceInKM;

    @Column(name = "portfolio_url", columnDefinition = "TEXT")
    private String portfolioUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "portfolio_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private PortfolioType portfolioType = PortfolioType.WEBSITE;

    @Column(name = "business_logo_url", columnDefinition = "TEXT")
    private String businessLogoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "average_rating", precision = 2, scale = 1)
    private BigDecimal averageRating = BigDecimal.valueOf(0.0);

    @Column(name = "number_of_reviews")
    private Integer numberOfReviews = 0;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @OneToOne(mappedBy = "serviceProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ServiceProviderTaxInfo taxInfo;

    private Boolean isVerified = false;
    private Boolean isActive = false;
    private Boolean isBlocked = false;

    @CreatedDate
    @Column(name = "registered_at", updatable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

}

