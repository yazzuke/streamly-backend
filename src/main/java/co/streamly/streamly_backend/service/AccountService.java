package co.streamly.streamly_backend.service;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.Account.AccountRepository;
import co.streamly.streamly_backend.domain.AccountPrice.AccountPrice;
import co.streamly.streamly_backend.domain.AccountPrice.AccountPriceRepository;
import co.streamly.streamly_backend.domain.AccountPrice.TypeAccount;
import co.streamly.streamly_backend.dto.AccountAdminDTO;
import co.streamly.streamly_backend.dto.AccountPriceDTO;
import co.streamly.streamly_backend.dto.AccountSummaryDTO;
import co.streamly.streamly_backend.domain.Metadata.ServiceMetadata;
import co.streamly.streamly_backend.domain.Metadata.ServiceMetadataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountPriceRepository accountPriceRepository;
    private final ServiceMetadataRepository serviceMetadataRepository;

    // Inyección de dependencia a través del constructor
    @Autowired
    public AccountService(AccountRepository accountRepository, AccountPriceRepository accountPriceRepository,
            ServiceMetadataRepository serviceMetadataRepository) {
        this.accountRepository = accountRepository;
        this.accountPriceRepository = accountPriceRepository;
        this.serviceMetadataRepository = serviceMetadataRepository;
    }

    public Account createAccount(Account account) {
        // Buscar metadatos por nombre del servicio
        Optional<ServiceMetadata> serviceMetadataOpt = serviceMetadataRepository
                .findByServiceName(account.getServiceName());

        // Si se encuentran metadatos, se asignan automáticamente la descripción, URL de
        // imagen y URL del SVG
        if (serviceMetadataOpt.isPresent()) {
            ServiceMetadata serviceMetadata = serviceMetadataOpt.get();
            account.setServiceName(serviceMetadata.getServiceName());
            account.setDescription(serviceMetadata.getDescription());
            account.setImageUrl(serviceMetadata.getImageUrl());
            account.setSvgUrl(serviceMetadata.getSvgUrl());
        }

        return accountRepository.save(account);
    }

    // Método para actualizar una cuenta existente
    public Optional<Account> updateAccount(Long id, Account accountDetails) {
        return accountRepository.findById(id).map(account -> {
            account.setServiceName(accountDetails.getServiceName());
            return accountRepository.save(account);
        });
    }

    // Método para eliminar una cuenta por ID
    public boolean deleteAccount(Long id) {
        return accountRepository.findById(id).map(account -> {
            accountRepository.delete(account);
            return true;
        }).orElse(false);
    }

    // Obtener todas las cuentas
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Obtener una cuenta por su ID
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    // Obtener cuentas por nombre del servicio
    public List<Account> getAccountsByServiceName(String serviceName) {
        return accountRepository.findByServiceName(serviceName);
    }

    // Método para obtener todos los precios asociados a una cuenta específica como
    // DTOs
    public List<AccountPriceDTO> getPricesByAccountId(Long accountId) {
        List<AccountPrice> prices = accountPriceRepository.findByAccountId(accountId);
        return prices.stream().map(AccountPriceDTO::new).collect(Collectors.toList());
    }

    // Método para crear un precio asociado a una cuenta específica
    public AccountPriceDTO createAccountPrice(Long accountId, AccountPrice accountPrice) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account no encontrada con id: " + accountId));
        accountPrice.setAccount(account);
        return new AccountPriceDTO(accountPriceRepository.save(accountPrice));
    }

    // Actualizar un precio específico
    public Optional<AccountPriceDTO> updateAccountPrice(Long priceId, AccountPrice priceDetails) {
        return accountPriceRepository.findById(priceId).map(price -> {
            price.setMonths(priceDetails.getMonths());
            price.setPrice(priceDetails.getPrice());
            AccountPrice updatedPrice = accountPriceRepository.save(price);
            return new AccountPriceDTO(updatedPrice);
        });
    }

    // Eliminar un precio asociado a una cuenta
    public boolean deleteAccountPrice(Long priceId) {
        return accountPriceRepository.findById(priceId).map(price -> {
            accountPriceRepository.delete(price);
            return true;
        }).orElse(false);
    }

    // Método para obtener todas las cuentas con el precio más bajo
    public List<AccountSummaryDTO> getAllAccountsSummaryWithLowestPrice() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> {
            Double lowestPrice = accountPriceRepository.findTopByAccountIdOrderByPriceAsc(account.getId())
                    .map(AccountPrice::getPrice)
                    .orElse(null);
            return new AccountSummaryDTO(account, lowestPrice);
        }).collect(Collectors.toList());
    }

    // Método para obtener el precio asociado a una cuenta específica
    public Optional<AccountPriceDTO> getAccountPrice(Long accountId, int months, TypeAccount type) {
        return accountPriceRepository.findByAccountIdAndMonthsAndType(accountId, months, type)
                .map(AccountPriceDTO::new);
    }

     // Método para obtener todas las cuentas con todos sus detalles para el admin
    public List<AccountAdminDTO> getAllAccountsForAdmin() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> {
            List<AccountPriceDTO> prices = getPricesByAccountId(account.getId());
            return new AccountAdminDTO(account, prices);
        }).collect(Collectors.toList());
    }
    
}
