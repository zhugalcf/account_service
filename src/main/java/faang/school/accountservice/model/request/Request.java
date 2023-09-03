package faang.school.accountservice.model.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @Column(name = "lock_value", nullable = false)
    private String lockValue;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;

    @Column(name = "input_data")
    private String inputData;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private RequestStatus requestStatus;

    @Column(name = "status_details")
    private String statusDetails;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
