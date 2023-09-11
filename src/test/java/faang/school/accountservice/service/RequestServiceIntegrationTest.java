package faang.school.accountservice.service;

import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.exception.IdempotencyException;
import faang.school.accountservice.model.request.RequestStatus;
import faang.school.accountservice.model.request.RequestType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class RequestServiceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgreSqlContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("test_db")
                    .withUsername("test_user")
                    .withPassword("test_password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSqlContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSqlContainer::getPassword);
        registry.add("spring.datasource.username", postgreSqlContainer::getUsername);
    }

    @Autowired
    private RequestService requestService;
    private CreateRequestDto createRequestDto;
    private RequestDto requestResult;
    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        Map<String, Object> exampleInput = new HashMap<>();
        exampleInput.put("1", "1");
        createRequestDto = CreateRequestDto.builder()
                .requestType(RequestType.DEPOSIT_FUNDS)
                .ownerType(OwnerType.USER)
                .idempotencyKey(UUID.fromString("5a8a12c5-3fc0-4f1a-80a5-d9e3a977d953"))
                .inputData(exampleInput)
                .ownerId(1L)
                .build();
        requestDto = RequestDto.builder()
                .id(1L)
                .requestType(RequestType.DEPOSIT_FUNDS)
                .ownerType(OwnerType.USER)
                .idempotencyKey(UUID.fromString("5a8a12c5-3fc0-4f1a-80a5-d9e3a977d953"))
                .inputData(exampleInput)
                .ownerId(1L)
                .requestStatus(RequestStatus.TO_DO)
                .isOpen(true)
                .version(0L)
                .build();
    }

    @AfterAll
    static void setAfterAll() {
        postgreSqlContainer.close();
    }

    @Test
    public void testRequestWithSameTokenIsNotCreating() {
        RequestDto requestResult = requestService.getOrSave(createRequestDto);
        Assertions.assertEquals(requestDto.getIdempotencyKey(), requestResult.getIdempotencyKey());
        RequestDto sameRequestResult = requestService.getOrSave(createRequestDto);
        Assertions.assertEquals(requestResult.getId(), sameRequestResult.getId());
    }

    @Test
    public void testAnotherRequestWithSameTokenThrowsIdempotencyException() {
        requestService.getOrSave(createRequestDto);
        createRequestDto.setInputData(new HashMap<>());
        Assertions.assertThrows(IdempotencyException.class, () -> requestService.getOrSave(createRequestDto));
    }

    @Test
    public void testAnotherRequestWithLockValueNotCreated() {
        UUID newRequestKeyWithLockValue = UUID.fromString("6a8a12c5-3fc0-4f1a-80a5-d9e3a977d953");
        createRequestDto.setIdempotencyKey(newRequestKeyWithLockValue);
        createRequestDto.setLockValue("1");
        requestService.getOrSave(createRequestDto);
        UUID newRequestKeyWithSameLockValue = UUID.fromString("7a8a12c5-3fc0-4f1a-80a5-d9e3a977d953");
        createRequestDto.setIdempotencyKey(newRequestKeyWithSameLockValue);
        Assertions.assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> requestService.getOrSave(createRequestDto));
    }

    @Test
    public void testRequestIsUpdating() {
        String statusDetails = "Some details";
        RequestDto requestResult = requestService
                .updateRequestStatus(1L, RequestStatus.DONE, statusDetails);
        Assertions.assertEquals(RequestStatus.DONE, requestResult.getRequestStatus());
        Assertions.assertEquals(statusDetails, requestResult.getStatusDetails());
        Assertions.assertFalse(requestResult.isOpen());
    }
}
