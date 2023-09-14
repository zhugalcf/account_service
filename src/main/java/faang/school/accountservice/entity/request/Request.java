package faang.school.accountservice.entity.request;

import faang.school.accountservice.converter.MapToJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private RequestType requestType;

    @NotNull
    @Column(name = "lock_value", nullable = false)
    private Long lockValue;

    @NotNull
    @Builder.Default
    @Column(name = "is_open", nullable = false)
    private Boolean isOpen = false;

    @Column(name = "input_data")
    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> inputData;

    @NotNull
    @Column(name = "request_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private RequestStatus requestStatus;

    @Column(name = "status_details", length = Integer.MAX_VALUE)
    private String statusDetails;

    @NotNull
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    @UpdateTimestamp
    private Instant lastModified;

    @NotNull
    @Column(name = "version", nullable = false)
    @Version
    private Long version;
}
