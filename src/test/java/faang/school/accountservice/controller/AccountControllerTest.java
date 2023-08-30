//package faang.school.accountservice.controller;
//
//import faang.school.accountservice.config.context.UserContext;
//import faang.school.accountservice.dto.AccountDto;
//import faang.school.accountservice.model.AccountStatus;
//import faang.school.accountservice.model.AccountType;
//import faang.school.accountservice.model.Currency;
//import faang.school.accountservice.service.AccountService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.when;
//
//@WebMvcTest(AccountController.class)
//class AccountControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserContext userContext;
//
//    @MockBean
//    private AccountService accountService;
//
//    @Test
//    void get() {
//        when(accountService.get(1L)).thenReturn(mockAccountDto());
//
//        Assertions.assertEquals(1, 1);
//    }
//
//    private AccountDto mockAccountDto() {
//        return AccountDto.builder()
//                .id(1L)
//                .ownerId(1L)
//                .number("123")
//                .status(AccountStatus.ACTIVE)
//                .type(AccountType.CURRENT_ACCOUNT)
//                .currency(Currency.USD)
//                .version(1L)
//                .build();
//    }
//}
