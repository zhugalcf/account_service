package faang.school.accountservice.controller;

import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.service.RequestService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/request")
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(@RequestBody @Valid RequestDto requestDto) {
        return requestService.createRequest(requestDto);
    }
}
