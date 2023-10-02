package faang.school.accountservice.model;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.mapper.JsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private RequestType requestType;
    private String lockValue;
    @NotNull
    private Boolean isOpenRequest;
    @Column(columnDefinition = "input_data")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> inputData;
    @NotNull
    private RequestStatus requestStatus;
    private String additionalData;
    private ZonedDateTime createdAt;
    private  ZonedDateTime updatedAt;
    @Version
    private int version;

}
