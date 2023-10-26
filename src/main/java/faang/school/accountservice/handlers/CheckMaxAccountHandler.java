package faang.school.accountservice.handlers;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
import faang.school.accountservice.exception.AlreadyExistException;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckMaxAccountHandler implements RequestTaskHandler<String, Object> {
    private final AccountRepository accountRepository;
    @Override
    public Map<String, Object> execute(Request request, Map<String, Object> context) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findByOwner_Id(request.getUserId()).orElseThrow(() -> new AlreadyExistException("User with ID " + request.getUserId() + "already have account")));
        return context;
    }

    @Override
    public RequestHandler getHandlerId() {
        return RequestHandler.CHECK_MAX_ACCOUNTS;
    }
}
