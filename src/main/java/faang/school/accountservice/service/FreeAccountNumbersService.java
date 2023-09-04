package faang.school.accountservice.service;

import faang.school.accountservice.repository.AccountNumberSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreeAccountNumbersService {
    private final FreeAccountNumberRepository freeAccountNumberRepository;
    private final AccountNumberSequenceRepository accountNumberSequenceRepository;
}
