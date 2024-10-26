package co.streamly.streamly_backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.AccountPrice.AccountPrice;
import co.streamly.streamly_backend.domain.AccountPrice.TypeAccount;
import co.streamly.streamly_backend.dto.AccountPriceDTO;
import co.streamly.streamly_backend.dto.AccountSummaryDTO;
import co.streamly.streamly_backend.service.AccountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/streamly/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

   // Obtener resumen de todas las cuentas con el precio más bajo
    @GetMapping
    public ResponseEntity<List<AccountSummaryDTO>> getAllAccountsSummary() {
        List<AccountSummaryDTO> accountsSummary = accountService.getAllAccountsSummaryWithLowestPrice();
        return ResponseEntity.ok(accountsSummary);
    }

    // Obtener una cuenta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva cuenta
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    // Actualizar una cuenta existente
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account accountDetails) {
        Optional<Account> updatedAccount = accountService.updateAccount(id, accountDetails);
        return updatedAccount.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una cuenta por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        boolean deleted = accountService.deleteAccount(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para crear un precio asociado a una cuenta específica
    @PostMapping("/{accountId}/prices")
    public ResponseEntity<AccountPriceDTO> createAccountPrice(@PathVariable Long accountId,
            @RequestBody AccountPrice accountPrice) {
        AccountPriceDTO createdPrice = accountService.createAccountPrice(accountId, accountPrice);
        return ResponseEntity.ok(createdPrice);
    }

    // Endpoint para obtener todos los precios de una cuenta específica
    @GetMapping("/{accountId}/prices")
    public ResponseEntity<List<AccountPriceDTO>> getPricesByAccountId(@PathVariable Long accountId) {
        List<AccountPriceDTO> prices = accountService.getPricesByAccountId(accountId);
        return ResponseEntity.ok(prices);
    }

    // Endpoint para actualizar un precio específico
    @PutMapping("/prices/{priceId}")
    public ResponseEntity<AccountPriceDTO> updateAccountPrice(@PathVariable Long priceId,
            @RequestBody AccountPrice priceDetails) {
        Optional<AccountPriceDTO> updatedPrice = accountService.updateAccountPrice(priceId, priceDetails);
        return updatedPrice.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para eliminar un precio específico
    @DeleteMapping("/prices/{priceId}")
    public ResponseEntity<Void> deleteAccountPrice(@PathVariable Long priceId) {
        boolean deleted = accountService.deleteAccountPrice(priceId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Endpoint para obtener el precio de una cuenta específica
        @GetMapping("/{accountId}/price")
    public ResponseEntity<AccountPriceDTO> getAccountPrice(
            @PathVariable Long accountId,
            @RequestParam int months,
            @RequestParam TypeAccount type) {

        Optional<AccountPriceDTO> accountPriceDTO = accountService.getAccountPrice(accountId, months, type);
        return accountPriceDTO.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }

    

}