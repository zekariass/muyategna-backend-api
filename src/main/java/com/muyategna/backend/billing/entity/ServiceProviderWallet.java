package com.muyategna.backend.billing.entity;

import com.muyategna.backend.service_provider.entity.ServiceProvider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_provider_wallet")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @OneToOne
    @JoinColumn(name = "paygo_credit_id")
    private PayGoCredit paygoCredit;

    @OneToOne
    @JoinColumn(name = "subscription_id")
    private ServiceProviderSubscription subscription;

    @Column(name = "total_credit", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCredit = BigDecimal.ZERO;

    @Column(name = "total_hold_credit", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHoldCredit = BigDecimal.ZERO;

    @Column(name = "available_credit", nullable = false, precision = 10, scale = 2)
    private BigDecimal availableCredit = BigDecimal.ZERO;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public boolean hasEnoughCredit(BigDecimal amount) {
        return availableCredit.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
    }
}
