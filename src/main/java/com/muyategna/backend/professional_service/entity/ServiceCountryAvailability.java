package com.muyategna.backend.professional_service.entity;

import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.professional_service.enums.PriceModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_country_availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCountryAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriceModel priceModel = PriceModel.QUOTE;

    private BigDecimal basePrice;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

