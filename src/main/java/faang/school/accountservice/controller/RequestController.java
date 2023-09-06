package faang.school.accountservice.controller;

import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.ResponseRequestDto;
import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.dto.request.UpdateRequestDto;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests")
public class RequestController {
    private final RequestService requestService;
    private final UserContext userContext;

    @GetMapping("/{userId}")
    private List<ResponseRequestDto> getByUserId(@PathVariable Long userId){
        return requestService.getByUserId(userId);
    }

    @PostMapping
    private ResponseRequestDto createRequest(@RequestBody @Validated CreateRequestDto requestDto){
        requestDto.setUserId(userContext.getUserId());
        return requestService.createRequest(requestDto);
    }

    @PutMapping
    private ResponseRequestDto updateRequest(@RequestBody @Validated UpdateRequestDto requestDto){
        requestDto.setUserId(userContext.getUserId());
        return requestService.updateRequest(requestDto);
    }

    @PutMapping("/open")
    private ResponseRequestDto openRequest(@RequestBody @Validated OpenRequestDto requestDto){
        requestDto.setUserId(userContext.getUserId());
        return requestService.openRequest(requestDto);
    }
}
