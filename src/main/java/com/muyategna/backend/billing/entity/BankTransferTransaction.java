package com.muyategna.backend.billing.entity;


import com.muyategna.backend.billing.enums.BankTransferStatus;
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
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "bank_transfer_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BankTransferTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(name = "txn_ref", nullable = false, length = 50, unique = true)
    private String txnRef;

    @ManyToOne
    @JoinColumn(name = "payee_bank_account_id", referencedColumnName = "id")
    private PlatformBankInformation payeeBankInfo;

    @Column(name = "payer_full_name", nullable = false, length = 100)
    private String payerFullName;

    @Column(name = "payer_bank_name", length = 100)
    private String payerBankName;

    @Column(name = "payer_bank_branch_name", length = 100)
    private String payerBankBranchName;

    @Column(name = "payer_bank_account_number", length = 50)
    private String payerBankAccountNumber;

    @Column(name = "amount_due", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountDue;

    @Column(name = "amount_paid", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "bank_reference_number", length = 50, unique = true)
    private String bankReferenceNumber;

    @Column(name = "proof_of_payment_url", columnDefinition = "TEXT")
    private String proofOfPaymentUrl;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private BankTransferStatus status;

    @Type(JsonBinaryType.class)
    @Column(name = "extra_data", columnDefinition = "jsonb")
    private Map<String, Object> extraData;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

}

