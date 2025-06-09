package com.muyategna.backend.service_provider.entity;

import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.enums.PortfolioType;
import com.muyategna.backend.user.enums.ServiceProviderType;
import com.muyategna.backend.user.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_provider_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_profile"))
    private UserProfile userProfile;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_description", columnDefinition = "TEXT")
    private String businessDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_provider_type", nullable = false)
    private ServiceProviderType providerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_address_id", foreignKey = @ForeignKey(name = "fk_business_address"))
    private Address businessAddress;

    @Column(name = "max_travel_distance_in_km")
    private Integer maxTravelDistanceInKm;

    @Column(name = "num_employees")
    private Integer numEmployees;

    @Column(name = "portfolio_url", columnDefinition = "TEXT")
    private String portfolioUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "portfolio_type")
    private PortfolioType portfolioType;

    @Column(name = "business_logo_url", columnDefinition = "TEXT")
    private String businessLogoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt = LocalDateTime.now();
}
