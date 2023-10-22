package faang.school.accountservice.handlers;

import faang.school.accountservice.dto.account.CreateAccountDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateAccountHandler implements RequestTaskHandler<String, Object>{
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    @Override
    public Map<String, Object> execute(Request request, Map<String, Object> context) {
        Map<String, Object> input = request.getInput();
        CreateAccountDto createAccountDto = (CreateAccountDto) input.get("createAccountDto");
        Account account = accountRepository.save(accountMapper.createDtoToEntity(createAccountDto));
        context.put("accountId", account.getId());
        context.put("ownerId", account.getOwner().getOwnerId());
        context.put("number", account.getNumber());
        return context;
    }

    @Override
    public RequestHandler getHandlerId() {
        return RequestHandler.CREATE_ACCOUNT;
    }
}
