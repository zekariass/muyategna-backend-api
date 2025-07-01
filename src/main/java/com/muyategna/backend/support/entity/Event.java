package com.muyategna.backend.support.entity;

import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.support.enums.EventMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @ManyToOne
    @JoinColumn(name = "event_location_id", referencedColumnName = "address_id")
    private Address eventLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_mode", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EventMode eventMode;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "is_active")
    private boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
