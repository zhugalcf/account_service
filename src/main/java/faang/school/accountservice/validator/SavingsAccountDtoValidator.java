package faang.school.accountservice.validator;

import faang.school.accountservice.dto.SavingsAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class SavingsAccountDtoValidator {
    public void validate(SavingsAccountDto savingsAccountDto, Errors errors) {
        if (savingsAccountDto.getAccountId() == null) {
            errors.rejectValue("accountId", "field.required", "Account ID is required");
        }
    }
}