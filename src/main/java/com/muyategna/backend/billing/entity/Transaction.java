package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.TransactionStatusEnum;
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
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_intent_id")
    private PaymentIntent paymentIntent;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigDecimal subTotalAmount;

    @Column(nullable = false)
    private BigDecimal taxAmount;

    @Column(nullable = false)
    private BigDecimal amountPaid;

    @Column(nullable = false)
    private String txnReference;

    @Column(nullable = false)
    private String currency;
    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status = TransactionStatusEnum.INITIATED;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "transaction")
    private ServiceProviderInvoice invoice;
}
