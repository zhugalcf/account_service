package faang.school.accountservice.controller;

import faang.school.accountservice.dto.CreateRequestDto;
import faang.school.accountservice.dto.ResponseRequestDto;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests/")
public class RequestController {
    private final RequestService requestService;

    @GetMapping("{userId}")
    private List<ResponseRequestDto> getByUserId(@PathVariable Long userId){
        return requestService.getByUserId(userId);
    }

    @PostMapping
    private CreateRequestDto test(@RequestBody CreateRequestDto requestDto){
        return requestDto;
    }
}
