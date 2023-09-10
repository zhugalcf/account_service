package faang.school.accountservice.service;

import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.excpetion.EntityNotFoundException;
import faang.school.accountservice.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public Owner getOwner(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner with id " + id + " not found"));
    }
}
