package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.enums.Status;
import faang.school.accountservice.repository.OwnerRepository;
import faang.school.accountservice.service.AccountService;
import faang.school.accountservice.util.BaseContextTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AccountControllerTest extends BaseContextTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private OwnerRepository ownerRepository;
    private AccountDto accountDto;
    private AccountDto accountDtoCreated;

    @BeforeAll
    void beforeAll() {
        accountDto = AccountDto.builder()
                .numberPayment("123456789012345")
                .ownerId(1L)
                .type(AccountType.CREDIT_ACCOUNT)
                .currency(Currency.EUR)
                .status(Status.ACTIVE)
                .build();

        ownerRepository.save(Owner.builder().id(1L).type(OwnerType.USER).ownerId(1L).build());
        accountService.open(accountDto);

    }

    @BeforeEach
    void setUp() {
        accountDtoCreated = AccountDto.builder()
                .id(1L)
                .numberPayment("123456789012345")
                .ownerId(1L)
                .type(AccountType.CREDIT_ACCOUNT)
                .currency(Currency.EUR)
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    @Transactional
    void testOpen() throws Exception {
        accountDto.setNumberPayment("1111122222333");
        accountDtoCreated.setNumberPayment("1111122222333");
        accountDtoCreated.setId(2L);
        mockMvc.perform(post("/account/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
    }

    @Test
    @Transactional
    void testGet() throws Exception {
        mockMvc.perform(get("/account/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
    }

    @Test
    @Transactional
    void testBlock() throws Exception {
        accountDtoCreated.setStatus(Status.FROZEN);

        mockMvc.perform(patch("/account/1/block")
                        .param("status", "FROZEN"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
    }

    @Test
    @Transactional
    void testClose() throws Exception {
        accountDtoCreated.setStatus(Status.CLOSED);
        mockMvc.perform(patch("/account/1/close"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
    }
}