package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.Currency;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.enums.Status;
import faang.school.accountservice.repository.CurrencyRepository;
import faang.school.accountservice.repository.OwnerRepository;
import faang.school.accountservice.service.AccountService;
import faang.school.accountservice.util.BaseContextTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerTest extends BaseContextTest {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private AccountService accountService;

    private AccountDto accountDto;
    private AccountDto accountDtoCreated;

//    @BeforeAll
//    void beforeAll() {
//
//        currencyRepository.save(Currency.builder().currency("USD").build());
//        ownerRepository.save(Owner.builder().ownerId(1L).type(OwnerType.USER).build());
//    }

    @BeforeEach
    void setUp() {
        accountDto = AccountDto.builder()
                .type(AccountType.CREDIT_ACCOUNT)
                .status(Status.ACTIVE)
                .build();

        accountDtoCreated = AccountDto.builder()
                .id(1L)
                .type(AccountType.CREDIT_ACCOUNT)
                .status(Status.ACTIVE)
                .build();

        Currency usd = currencyRepository.save(Currency.builder().currencyName("USD").build());
        Owner save = ownerRepository.save(Owner.builder().ownerId(1L).type(OwnerType.USER).build());
        accountDto.setCurrencyId(usd.getId());
        accountDto.setOwnerId(save.getId());
        accountDtoCreated.setCurrencyId(usd.getId());
        accountDtoCreated.setOwnerId(save.getId());
    }

//    @Test
//    @Transactional
//    void testOpen() throws Exception {
//        mockMvc.perform(post("/account/open")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
//    }

    @Test
    @Transactional
    void testGet() throws Exception {
        accountDto.setAccountNumber("12345678901232");
        accountDtoCreated.setAccountNumber("12345678901232");

        AccountDto open = accountService.open(accountDto);
        accountDtoCreated.setId(open.getId());

        mockMvc.perform(get("/account/{id}", open.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
    }

    @Test
    @Transactional
    void testBlock() throws Exception {
        accountDto.setAccountNumber("12345678901233");
        accountDtoCreated.setAccountNumber("12345678901233");
        accountDtoCreated.setStatus(Status.FROZEN);
        AccountDto open = accountService.open(accountDto);
        accountDtoCreated.setId(open.getId());

        mockMvc.perform(patch("/account/{id}/block", open.getId())
                        .param("status", "FROZEN"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
    }

    @Test
    @Transactional
    void testClose() throws Exception {
        accountDto.setAccountNumber("12345678901234");
        accountDtoCreated.setAccountNumber("12345678901234");
        accountDtoCreated.setStatus(Status.CLOSED);
        AccountDto open = accountService.open(accountDto);
        accountDtoCreated.setId(open.getId());

        mockMvc.perform(patch("/account/{id}/close", open.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountDtoCreated)));
    }
}