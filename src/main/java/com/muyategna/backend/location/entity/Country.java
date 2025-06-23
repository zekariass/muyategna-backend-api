package com.muyategna.backend.location.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a country.
 * <p>
 * This entity is used to store information about countries, including their name and timestamps for creation and updates.
 * </p>
 */
@Entity
@Table(name = "country")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "iso_code2", length = 2, nullable = false, unique = true)
    private String countryIsoCode2;

    @Column(name = "iso_code3", length = 3, unique = true)
    private String countryIsoCode3;

    @Column(name = "iso_code_numeric", length = 3, unique = true)
    private String countryIsoCodeNumeric;

    @Column(name = "continent")
    private String continent;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Region> regions;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CountryTranslation> translations;

    private boolean isGlobal;

    private String currency;

    private String taxpayerIdType;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}

