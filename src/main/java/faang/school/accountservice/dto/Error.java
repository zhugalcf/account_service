package faang.school.accountservice.dto;

import lombok.Builder;

@Builder
public record Error(String code, String message) {
}
