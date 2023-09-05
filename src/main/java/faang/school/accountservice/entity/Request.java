package faang.school.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.util.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "request")
public class Request {
    @Id
    private UUID idempotentToken;

    @Column(name = "username", nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Column(name = "lock_value", nullable = false)
    private Long lockValue;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "input_data")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> input_data;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

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

    @Column(name = "version", nullable = false)
    private Long version;
}
