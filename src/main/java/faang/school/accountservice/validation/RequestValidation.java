package faang.school.accountservice.validation;

import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestValidation {
    public boolean validateOpeningRequest(OpenRequestDto openRequestDto, Request request) {
        return !request.getStatus().equals(RequestStatus.WAITING) ||
                request.getLock().equals(openRequestDto.getLock());
    }

    public boolean checkRelevance(Request request) {
        return request.getStatus() == RequestStatus.CANCELLED
                || request.getStatus() == RequestStatus.EXECUTED;
    }

    public boolean checkCurrentStatus(Request request) {
        return request.getStatus().equals(RequestStatus.EXECUTED)
                || request.getStatus().equals(RequestStatus.CANCELLED);
    }

    public boolean checkTypeOfStatusToChange(int ordinaryStatus, Request changingStatus) {
        if (changingStatus.getStatus().equals(RequestStatus.IN_PROGRESS)
                && RequestStatus.of(ordinaryStatus).equals(RequestStatus.EXECUTED)) {
            return false;
        }
        return changingStatus.getStatus().equals(RequestStatus.IN_PROGRESS)
                || !RequestStatus.of(ordinaryStatus).equals(RequestStatus.CANCELLED);
    }
}
