package faang.school.accountservice.exception;

import faang.school.accountservice.entity.account.AccountType;
import lombok.AllArgsConstructor;

import java.text.MessageFormat;

@AllArgsConstructor
public class TypeNotFoundException extends RuntimeException{
    private AccountType accountType;

    @Override
    public String getMessage() {
        return MessageFormat.format("Account type {0} not found", accountType.getValue());
    }
}
