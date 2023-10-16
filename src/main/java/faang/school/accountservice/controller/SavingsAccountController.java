package faang.school.accountservice.controller;

import faang.school.accountservice.dto.SavingsAccountCreateDto;
import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.dto.SavingsAccountUpdateDto;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.service.SavingsAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/savings/account")
@Slf4j
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @PostMapping("/open")
    public SavingsAccountResponseDto openSavingsAccount(@RequestBody @Validated SavingsAccountCreateDto savingsAccountCreateDto) {
        log.info("Received request to open account for savings by account id = {}", savingsAccountCreateDto.getAccountId());
        return savingsAccountService.openSavingsAccount(savingsAccountCreateDto);
    }

    @PutMapping("/funds")
    public SavingsAccountResponseDto addFundsToSavingsAccount(@RequestBody @Validated SavingsAccountUpdateDto updateDto){
        log.info("Received request to add funds for savings account with id = {}, money amount = {}", updateDto.getSavingsAccountId(), updateDto.getMoneyAmount());
        return savingsAccountService.addFundsToSavingsAccount(updateDto);
    }

    @PutMapping("/update")
    public SavingsAccountResponseDto changeSavingsAccountTariff(@RequestBody @Validated SavingsAccountUpdateDto updateDto) {
        log.info("Received request to change tariff of savings account with id: {}, to a new tariff: {}",
                updateDto.getSavingsAccountId(), updateDto.getTariffType());
        return savingsAccountService.changeSavingsAccountTariff(updateDto);
    }

    @GetMapping("/{id}")
    public SavingsAccountResponseDto getSavingsAccountBy(@PathVariable long id) {
        log.info("Received request of getting savings account with id: {}", id);
        return savingsAccountService.getSavingsAccountDtoBy(id);
    }

    @GetMapping
    public SavingsAccountResponseDto getSavingsAccountByOwnerIdAndOwnerType(
            @RequestParam("ownerId") long ownerId,
            @RequestParam("ownerType") OwnerType ownerType) {
        log.info("Received request of getting savings account with owner id: {}, type: {}", ownerId, ownerType);
        return savingsAccountService.getSavingsAccountByOwner(ownerId, ownerType);
    }
}
