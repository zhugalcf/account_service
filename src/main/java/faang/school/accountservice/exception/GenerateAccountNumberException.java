package faang.school.accountservice.exception;

public class GenerateAccountNumberException extends RuntimeException{
    public GenerateAccountNumberException(String msg) {
        super(msg);
    }

    public GenerateAccountNumberException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
