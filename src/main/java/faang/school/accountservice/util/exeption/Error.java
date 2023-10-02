package faang.school.accountservice.util.exeption;

public class Error extends RuntimeException {
    public Error() {
    }

    public Error(String message) {
        super(message);
    }
}
