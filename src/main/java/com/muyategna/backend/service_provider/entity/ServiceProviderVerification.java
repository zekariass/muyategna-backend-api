package com.muyategna.backend.service_provider.entity;

import com.muyategna.backend.service_provider.enums.VerificationStatus;
import com.muyategna.backend.user.entity.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_provider_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProviderVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider serviceProvider;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private ServiceProviderVerificationType verificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "document_url", columnDefinition = "TEXT")
    private String documentUrl;

    @Column(name = "reason_for_rejection", columnDefinition = "TEXT")
    private String reasonForRejection;

    @Column(name = "verification_note", columnDefinition = "TEXT")
    private String verificationNote;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "verified_by")
    private UserProfile verifiedBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}