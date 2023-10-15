package faang.school.accountservice.entity;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID requestId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "type", nullable = false)
    private RequestType requestType;

    @Column(name = "lock")
    private Long lock;

    @Column(name = "is_open", nullable = false)
    private boolean open;

    @Column(name = "input", nullable = false)
    private String input;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private RequestStatus status;

    @Column(name = "additionally")
    private String additionally;

    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;
}
