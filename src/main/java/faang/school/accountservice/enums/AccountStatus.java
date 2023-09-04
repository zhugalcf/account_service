package faang.school.accountservice.enums;

public enum AccountStatus {
    OPEN("open"),
    FROZEN("frozen"),
    BLOCKED("blocked"),
    CLOSED("closed"),
    ;
    private final String status;

    AccountStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
