package faang.school.accountservice.controller;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/new")
    public void postRequest(@RequestBody RequestDto requestDto) {
        requestService.postRequest(requestDto);
    }

    @GetMapping("/by_user/{ownerId}")
    public List<RequestDto> getRequestByUser(@PathVariable Long ownerId) {
        return requestService.getRequestByUser(ownerId);
    }
}
