package com.muyategna.backend.billing.entity;

import com.muyategna.backend.service_provider.entity.ServiceProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "service_provider_subscription")
public class ServiceProviderSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @ManyToOne
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

//    @ManyToOne
//    @JoinColumn(name = "payment_intent_id", nullable = false)
//    private PaymentIntent paymentIntent;

    @Column(name = "initial_credits", nullable = false)
    private BigDecimal initialCredits;

    @Column(name = "used_credits", nullable = false)
    private BigDecimal usedCredits;

    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt;

//    @Column(name = "valid_until", nullable = false)
//    private LocalDateTime validUntil;

    @ManyToOne
    @JoinColumn(name = "upgraded_from")
    private ServiceProviderSubscription upgradedFrom;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    private LocalDateTime expiresAt;

}
