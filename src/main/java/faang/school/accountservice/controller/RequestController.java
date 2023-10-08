package faang.school.accountservice.controller;

import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests/")
public class RequestController {
    private final RequestService requestService;
}
