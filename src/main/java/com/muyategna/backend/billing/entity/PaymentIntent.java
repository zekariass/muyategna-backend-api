package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.PayerEntityTypeEnum;
import com.muyategna.backend.billing.enums.PaymentIntentStatusEnum;
import com.muyategna.backend.billing.enums.ProductTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payment_intent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PaymentIntent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayerEntityTypeEnum payerEntityType;

    private Long payerEntityId;
    private BigDecimal amountBeforeTax = BigDecimal.ZERO;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductTypeEnum productType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentIntentStatusEnum status;

    @Column(nullable = false)
    private LocalDateTime paymentDueAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "paymentIntent")
    private List<Transaction> transactions;
}
