package faang.school.accountservice.service;

import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.excpetion.EntityNotFoundException;
import faang.school.accountservice.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OwnerServiceTest {
    @InjectMocks
    private OwnerService ownerService;
    @Mock
    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        Owner owner = Owner.builder().id(1L).build();

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
    }

    @Test
    void getOwner_shouldInvokeRepositoryFindById() {
        ownerService.getOwner(1L);
        verify(ownerRepository).findById(1L);
    }

    @Test
    void getOwner_shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> ownerService.getOwner(2L),
                "Owner with id 2 not found");
    }
}