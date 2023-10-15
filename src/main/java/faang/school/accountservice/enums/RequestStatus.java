package faang.school.accountservice.enums;

public enum RequestStatus {
    WAITING,
    TO_EXECUTE,
    IN_PROGRESS,
    EXECUTED,
    CANCELLED;

    public static RequestStatus of(int status) {
        for (RequestStatus requestStatus : RequestStatus.values()) {
            if (requestStatus.ordinal() == status) {
                return requestStatus;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + status);
    }
}
