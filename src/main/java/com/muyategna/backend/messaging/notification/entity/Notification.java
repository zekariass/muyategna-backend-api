package com.muyategna.backend.messaging.notification.entity;

import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;
import com.muyategna.backend.messaging.notification.enums.NotificationStatus;
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

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_entity_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private NotificationRecipientType recipientEntityType;

    @Column(name = "recipient_entity_id", nullable = false)
    private Long recipientEntityId; // it can be user_profile_id or service_provider_id depending on recipient_entity_type

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_template_id", nullable = false)
    private NotificationTemplate notificationTemplate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_channel_id", nullable = false)
    private NotificationChannel notificationChannel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_category_id", nullable = false)
    private NotificationCategory notificationCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "payload", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, Object> payload;

    @Column(name = "subject")
    private String subject;

    @Lob
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @CreatedDate
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    private String errorMessage;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}



