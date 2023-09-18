package faang.school.accountservice.model.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RequestStatus {
    TO_DO(false),
    IN_PROGRESS(false),
    DONE(true),
    ERROR(false),
    FAILURE(true);

    private final boolean terminated;
    public boolean isTerminated() {
        return terminated;
    }
}
