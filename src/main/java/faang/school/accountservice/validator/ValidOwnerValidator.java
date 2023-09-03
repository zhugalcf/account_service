package faang.school.accountservice.validator;

import faang.school.accountservice.annotations.ValidOwner;
import faang.school.accountservice.client.ProjectServiceClient;
import faang.school.accountservice.client.UserServiceClient;
import faang.school.accountservice.dto.account.CreateAccountDto;
import faang.school.accountservice.enums.OwnerType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidOwnerValidator implements ConstraintValidator<ValidOwner, CreateAccountDto> {
    private final UserServiceClient userServiceClient;
    private final ProjectServiceClient projectServiceClient;

    @Override
    public boolean isValid(CreateAccountDto createAccountDto, ConstraintValidatorContext context) {
        if (createAccountDto.getOwnerType() == OwnerType.USER) {
            return userServiceClient.checkUserExist(createAccountDto.getOwnerId());
        } else if (createAccountDto.getOwnerType() == OwnerType.PROJECT) {
            return projectServiceClient.checkProjectExist(createAccountDto.getOwnerId());
        }
        return false;
    }
}

