package faang.school.accountservice.controller;

import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.dto.UpdateRequestDto;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/request")
public class RequestController {
    private final RequestService requestService;
    private final UserContext userContext;

    @PostMapping
    public RequestDto createRequest(@RequestBody RequestDto requestDto){
        requestDto.setUserId(userContext.getUserId());
        return requestService.createRequest(requestDto);
    }

    @PutMapping
    public UpdateRequestDto openRequest(@RequestBody UpdateRequestDto updateRequestDto){
        updateRequestDto.setUserId(userContext.getUserId());
        return requestService.openRequest(updateRequestDto);
    }

    @PutMapping("/update")
    public UpdateRequestDto updateRequest(@RequestBody UpdateRequestDto updateRequestDto){
        updateRequestDto.setUserId(userContext.getUserId());
        return requestService.updateRequest(updateRequestDto);
    }

    @GetMapping("/{userId}")
    private List<RequestDto> getByUserId(@PathVariable Long userId){
        return requestService.getByUserId(userId);
    }
}
