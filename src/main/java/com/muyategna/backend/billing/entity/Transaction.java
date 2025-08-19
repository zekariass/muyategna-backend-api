package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.AccountingTypeEnum;
import com.muyategna.backend.billing.enums.TransactionStatusEnum;
import com.muyategna.backend.billing.enums.TransactionTypeEnum;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "transaction")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private AccountingTypeEnum accountingType;

    @Column(nullable = false)
    private BigDecimal amountPaid;

    @Column(nullable = false)
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    @Column(nullable = false, length = 100)
    private String txnRef;

//    private String gatewayRef;

    @Column(name = "webhook_response_map", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, Object> webhookResponseMap;

    @Column(name = "is_txn_verified", nullable = false)
    private Boolean isTxnVerified = false;

    @Column(name = "txn_verification_response_map", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, Object> txnVerificationResponseMap;

    @OneToOne
    @JoinColumn(name = "original_txn_id", nullable = true)
    private Transaction originalTxn;

    @Column(length = 3, nullable = false)
    private String currency;

    private String description;
    private String responseCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TransactionTypeEnum txnType;

    @Column(name = "extra_data", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, Object> extraData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TransactionStatusEnum status = TransactionStatusEnum.INITIATED;

    @CreatedDate
    private LocalDateTime createdAt;
}
