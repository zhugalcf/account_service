package faang.school.accountservice.validate;

import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.dto.request.UpdateRequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestValidate {
    public boolean validateOpeningRequest(OpenRequestDto openRequestDto, Request request) {
        return !request.getStatus().equals(RequestStatus.WAITING) ||
                request.getLock().equals(openRequestDto.getLock());
    }

    public void validateClosureRequest(UpdateRequestDto checkDto, Request doUpdate) {
        if (checkDto.isClose()) {
            doUpdate.setStatus(RequestStatus.CANCELLED);
            doUpdate.setOpen(false);
            doUpdate.setLock(null);
        }
    }

    public boolean checkRelevance(Request request) {
        return request.getStatus() == RequestStatus.CANCELLED
                || request.getStatus() == RequestStatus.EXECUTED;
    }
}
