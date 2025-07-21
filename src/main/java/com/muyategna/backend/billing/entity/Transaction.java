package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.AccountingTypeEnum;
import com.muyategna.backend.billing.enums.TransactionStatusEnum;
import com.muyategna.backend.billing.enums.TxnTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
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
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TransactionStatusEnum status;


    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private AccountingTypeEnum accountingType;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TxnTypeEnum txnType;

    @Column(name = "refunded_amount")
    private BigDecimal refundedAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_txn_id")
    private Transaction originalTxnId;

    @Column(name = "response_code")
    private String responseCode;


    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "transaction")
    private ServiceProviderInvoice invoice;
}
