package faang.school.accountservice.controller;

import faang.school.accountservice.dto.request.CloseRequestDto;
import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.dto.request.UpdateRequestDto;
import faang.school.accountservice.service.RequestService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping
    public RequestDto updateRequest(@RequestBody @Valid UpdateRequestDto updateRequestDto) {
        return requestService.updateRequest(updateRequestDto);
    }

    @PutMapping("/open")
    public RequestDto openRequest(@RequestBody @Valid OpenRequestDto openRequestDto) {
        return requestService.openRequest(openRequestDto);
    }

    @PutMapping("/close")
    public RequestDto closeRequest(@RequestBody @Valid CloseRequestDto closeRequestDto) {
        return requestService.closeRequest(closeRequestDto);
    }
}
