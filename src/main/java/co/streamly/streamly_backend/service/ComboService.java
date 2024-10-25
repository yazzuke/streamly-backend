package co.streamly.streamly_backend.service;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.Account.AccountRepository;
import co.streamly.streamly_backend.domain.Combo.Combo;
import co.streamly.streamly_backend.domain.Combo.ComboRepository;
import co.streamly.streamly_backend.domain.ComboPrice.ComboPrice;
import co.streamly.streamly_backend.domain.ComboPrice.ComboPriceRepository;
import co.streamly.streamly_backend.domain.ComboPrice.TypeCombo;
import co.streamly.streamly_backend.domain.Metadata.ServiceMetadata;
import co.streamly.streamly_backend.domain.Metadata.ServiceMetadataRepository;
import co.streamly.streamly_backend.dto.AccountAdminDTO;
import co.streamly.streamly_backend.dto.AccountPriceDTO;
import co.streamly.streamly_backend.dto.ComboAdminDTO;
import co.streamly.streamly_backend.dto.ComboPriceDTO;
import co.streamly.streamly_backend.dto.ComboSummaryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComboService {

    private final ComboRepository comboRepository;
    private final ComboPriceRepository comboPriceRepository;
    private final ServiceMetadataRepository serviceMetadataRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ComboService(ComboRepository comboRepository,
            ComboPriceRepository comboPriceRepository,
            ServiceMetadataRepository serviceMetadataRepository,
            AccountRepository accountRepository) {
        this.comboRepository = comboRepository;
        this.comboPriceRepository = comboPriceRepository;
        this.serviceMetadataRepository = serviceMetadataRepository;
        this.accountRepository = accountRepository;
    }

    // Crear un nuevo combo
    public Combo createCombo(Combo combo) {
        // Recuperar las cuentas completas desde la base de datos usando los IDs
        // proporcionados
        if (combo.getAccounts() != null && !combo.getAccounts().isEmpty()) {
            List<Account> accountsWithMetadata = combo.getAccounts().stream().map(account -> {
                return accountRepository.findById(account.getId()).map(dbAccount -> {
                    // Buscar metadatos por nombre del servicio
                    Optional<ServiceMetadata> serviceMetadataOpt = serviceMetadataRepository
                            .findByServiceName(dbAccount.getServiceName());

                    // Si se encuentran metadatos, asignar el SVG correspondiente
                    serviceMetadataOpt.ifPresent(serviceMetadata -> {
                        dbAccount.setSvgUrl(serviceMetadata.getSvgUrl()); // Asignar el SVG URL
                    });

                    return dbAccount;
                }).orElse(account); // En caso de que la cuenta no se encuentre, devolver la original
            }).collect(Collectors.toList());

            combo.setAccounts(accountsWithMetadata);
        }

        return comboRepository.save(combo);
    }

    // Obtener todos los combos con el precio más bajo para el resumen
    public List<ComboSummaryDTO> getAllCombosSummaryWithLowestPrice() {
        List<Combo> combos = comboRepository.findAll();
        return combos.stream().map(combo -> {
            Double lowestPrice = comboPriceRepository.findTopByComboIdOrderByPriceAsc(combo.getId())
                    .map(ComboPrice::getPrice)
                    .orElse(null);
            return new ComboSummaryDTO(combo, lowestPrice);
        }).collect(Collectors.toList());
    }

    // Obtener el detalle de un combo incluyendo los SVGs de las cuentas asociadas
    public Optional<ComboSummaryDTO> getComboById(Long id) {
        return comboRepository.findById(id).map(combo -> {
            // Obtener solo los SVG URLs de las cuentas asociadas
            List<String> accountSvgs = combo.getAccounts().stream()
                    .map(Account::getSvgUrl) // Aquí se obtiene el `SVG` de cada cuenta
                    .collect(Collectors.toList());

            // Crear un DTO con la imagen principal del combo y los SVGs de las cuentas
            // asociadas
            ComboSummaryDTO comboSummary = new ComboSummaryDTO(combo, null);
            comboSummary.setAccountSvgs(accountSvgs); // Añadir los SVGs al DTO
            return comboSummary;
        });
    }

    // Crear un precio para un combo
    public ComboPriceDTO createComboPrice(Long comboId, ComboPrice comboPrice) {
        Combo combo = comboRepository.findById(comboId)
                .orElseThrow(() -> new RuntimeException("Combo no encontrado con id: " + comboId));
        comboPrice.setCombo(combo);
        return new ComboPriceDTO(comboPriceRepository.save(comboPrice));
    }

    // Obtener todos los precios de un combo específico
    public List<ComboPriceDTO> getPricesByComboId(Long comboId) {
        List<ComboPrice> prices = comboPriceRepository.findByComboId(comboId);
        return prices.stream().map(ComboPriceDTO::new).collect(Collectors.toList());
    }

    // Actualizar un combo específico
    public Optional<Combo> updateCombo(Long id, Combo comboDetails) {
        return comboRepository.findById(id).map(combo -> {
            combo.setName(comboDetails.getName());
            combo.setDescription(comboDetails.getDescription());
            combo.setPrice(comboDetails.getPrice());
            combo.setImageUrl(comboDetails.getImageUrl());
            return comboRepository.save(combo);
        });
    }

    // Eliminar un combo específico
    public boolean deleteCombo(Long id) {
        return comboRepository.findById(id).map(combo -> {
            comboRepository.delete(combo);
            return true;
        }).orElse(false);
    }

    // Obtener el precio de un combo específico
    public Optional<ComboPriceDTO> getComboPrice(Long comboId, int months, TypeCombo type) {
        return comboPriceRepository.findByComboIdAndMonthsAndType(comboId, months, type)
                .map(ComboPriceDTO::new);
    }

    // Obtener todos los combos
    public List<Combo> getAllCombos() {
        return comboRepository.findAll();
    }

    @Autowired
    private AccountService accountService;

    public List<ComboAdminDTO> getAllCombosForAdmin() {
        List<Combo> combos = comboRepository.findAll();
        return combos.stream().map(combo -> {
            List<ComboPrice> prices = comboPriceRepository.findByComboId(combo.getId());
            List<AccountAdminDTO> accountAdminDTOs = combo.getAccounts().stream().map(account -> {
                // Utilizar AccountService para obtener los precios de la cuenta
                List<AccountPriceDTO> pricesForAccount = accountService.getPricesByAccountId(account.getId());
                return new AccountAdminDTO(account, pricesForAccount);
            }).collect(Collectors.toList());
            return new ComboAdminDTO(combo, prices, accountAdminDTOs);
        }).collect(Collectors.toList());
    }

}
