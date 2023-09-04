package faang.school.accountservice.entity;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.util.MapToJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Table(
        name = "requests",
        indexes = @Index(name = "idx_user_id", columnList = "user_id"),
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"lock", "user_id"}, name = "uniq_lock_user_id")
        })
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    @Id
    private UUID idempotentToken;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private RequestType type;

    @Column(name = "lock")
    private String lock;

    @Column(name = "is_open")
    private boolean isOpen;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> input;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "details")
    private String details;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "opt_lock")
    private int version;
}
