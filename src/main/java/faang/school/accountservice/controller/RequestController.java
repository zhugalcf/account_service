package faang.school.accountservice.controller;

import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/request")
@RestController
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/{requestId}")
    public RequestDto getRequest(@PathVariable long requestId) {
        return requestService.getRequest(requestId);
    }

    @GetMapping("/by_owner/{ownerId}")
    public List<RequestDto> getRequestByOwner(@PathVariable Long ownerId, @RequestParam OwnerType ownerType) {
        return requestService.getRequestByOwner(ownerId, ownerType);
    }
}
