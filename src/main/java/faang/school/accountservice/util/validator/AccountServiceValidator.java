package faang.school.accountservice.util.validator;

import faang.school.accountservice.client.ProjectServiceClient;
import faang.school.accountservice.client.UserServiceClient;
import faang.school.accountservice.dto.account.AccountDto;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.util.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountServiceValidator {

    private final UserServiceClient userServiceClient;
    private final ProjectServiceClient projectServiceClient; // решил это тут заинжектить, потому что использую для проверки того, существуют ли такие сущности в сервисах

    public void validateToCreate(AccountDto dto) {
        if (dto.getUserId() != null && dto.getProjectId() != null ||
                dto.getUserId() == null && dto.getProjectId() == null) {
            throw new DataValidationException("Post's author can be only author or project and can't be both");
        }

        if (dto.getUserId() != null) {
            userServiceClient.getUser(dto.getUserId()); // проверяю есть ли такой юзер в юзер сервисе
        }
        if (dto.getProjectId() != null) {
            projectServiceClient.getProject(dto.getProjectId()); // проверяю есть ли такой проект в проект сервисе
        }
    }
}
