package com.muyategna.backend.billing.entity;

import com.muyategna.backend.service_provider.entity.ServiceProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_provider_add_on")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProviderAddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "payment_intent_id", nullable = false)
//    private PaymentIntent paymentIntent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_on_plan_id", nullable = false)
    private AddOnPlan addOnPlan;

    @Column(name = "initial_credits", nullable = false)
    private BigDecimal initialCredits;

    @Column(name = "used_credits", nullable = false)
    private BigDecimal usedCredits;

    @CreatedDate
    @Column(name = "purchased_at", nullable = false)
    private LocalDateTime purchasedAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "upgraded_from")
//    private ServiceProviderAddOn upgradedFrom;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
