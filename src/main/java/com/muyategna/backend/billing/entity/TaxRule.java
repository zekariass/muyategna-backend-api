package com.muyategna.backend.billing.entity;

import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.entity.Region;
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

@Entity
@Table(name = "tax_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TaxRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    private String taxType; // VAT
    private String name;
    private String description;

    private BigDecimal percentageValue;

    private BigDecimal fixedValue;

    private Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime effectiveStartDate;

    @Column(nullable = false)
    private LocalDateTime effectiveEndDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}