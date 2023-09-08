package faang.school.accountservice.controller;

import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.model.request.RequestStatus;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/by_user/{ownerId}")
    public List<RequestDto> getRequestByUser(@PathVariable Long ownerId) {
        return requestService.getRequestByOwner(ownerId);
    }

    @PostMapping("/new")
    public RequestDto create(@RequestBody CreateRequestDto createRequestDto){
        return requestService.createRequest(createRequestDto);
    }

    @PutMapping("/update/{id}")
    public RequestDto update(@PathVariable long id){
        RequestStatus requestStatus = RequestStatus.DONE;
        String r = "ASGHGhj";
        return requestService.updateRequestStatus(id,requestStatus,r);
    }
}
