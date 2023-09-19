package faang.school.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class AccountResponseDto {
    private long id;
    private String accountNumber;
    private long ownerId;
    private long version;
    private LocalDateTime createdAt;
}
