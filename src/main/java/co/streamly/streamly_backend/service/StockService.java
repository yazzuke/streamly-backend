package co.streamly.streamly_backend.service;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.Account.AccountRepository;
import co.streamly.streamly_backend.domain.AccountPrice.TypeAccount;
import co.streamly.streamly_backend.domain.Stock.Profile;
import co.streamly.streamly_backend.domain.Stock.ProfileRepository;
import co.streamly.streamly_backend.domain.Stock.Stock;
import co.streamly.streamly_backend.domain.Stock.StockRepository;

import co.streamly.streamly_backend.dto.AccountStockDTO;
import co.streamly.streamly_backend.dto.ProfileDTO;
import co.streamly.streamly_backend.dto.StockRequestDTO;
import co.streamly.streamly_backend.dto.StockResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockService {

    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public StockService(StockRepository stockRepository, AccountRepository accountRepository, ProfileRepository profileRepository) {
        this.stockRepository = stockRepository;
        this.accountRepository = accountRepository;
        this.profileRepository = profileRepository;
    }

   
public StockResponseDTO createStock(StockRequestDTO stockRequestDTO) {
        // Fetch the Account object using the provided ID
        Account account = accountRepository.findById(stockRequestDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account no encontrada con id: " + stockRequestDTO.getAccountId()));
    
        Stock stock = new Stock();
        stock.setAccountId(account);
        try {
            stock.setType(TypeAccount.valueOf(capitalize(stockRequestDTO.getType()))); // Adjusted to match exact case
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de cuenta inválido: " + stockRequestDTO.getType());
        }
        stock.setEmail(stockRequestDTO.getEmail());
        stock.setPassword(stockRequestDTO.getPassword());
        stock.setIsAssigned(false);
        Stock savedStock = stockRepository.save(stock);

        if (stock.getType() == TypeAccount.Pantalla && stockRequestDTO.getProfiles() != null) {
            List<Profile> profiles = stockRequestDTO.getProfiles().stream().map(profileDTO -> {
                Profile profile = new Profile();
                profile.setStock(savedStock);
                profile.setProfileName(profileDTO.getProfileName());
                profile.setProfilePassword(profileDTO.getProfilePassword());
                profile.setIsAssigned(false);
                return profile;
            }).collect(Collectors.toList());

            profileRepository.saveAll(profiles);
            savedStock.setProfiles(profiles);
        } else {
            savedStock.setProfiles(new ArrayList<>()); // Inicializa perfiles a una lista vacía si no es PANTALLA
        }

        // Construimos y devolvemos el StockResponseDTO a partir del Stock guardado
        AccountStockDTO accountSummary = new AccountStockDTO(
                savedStock.getAccountId().getId(),
                savedStock.getAccountId().getServiceName(),
                savedStock.getAccountId().getSvgUrl()
        );

        List<ProfileDTO> profileDTOs = savedStock.getProfiles().stream()
                .map(profile -> new ProfileDTO(profile.getProfileName(), profile.getProfilePassword()))
                .collect(Collectors.toList());

        return new StockResponseDTO(
                savedStock.getId(),
                accountSummary,
                savedStock.getType().name(),
                savedStock.getEmail(),
                savedStock.getPassword(),
                savedStock.getIsAssigned(),
                profileDTOs
        );
    }

    // Helper method to capitalize the first letter
        private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

   // method to get all stocks
    public List<StockResponseDTO> getAllStocks() {
        return stockRepository.findAll().stream().map(stock -> {
            AccountStockDTO accountSummary = new AccountStockDTO(
                    stock.getAccountId().getId(),
                    stock.getAccountId().getServiceName(),
                    stock.getAccountId().getSvgUrl()
            );

            List<ProfileDTO> profiles = stock.getProfiles() != null ?
                    stock.getProfiles().stream()
                            .map(profile -> new ProfileDTO(profile.getProfileName(), profile.getProfilePassword()))
                            .collect(Collectors.toList()) :
                    new ArrayList<>();

            return new StockResponseDTO(
                    stock.getId(),
                    accountSummary,
                    stock.getType().name(),
                    stock.getEmail(),
                    stock.getPassword(),
                    stock.getIsAssigned(),
                    profiles
            );
        }).collect(Collectors.toList());
    }

    // method to get stock by id and return it as a DTO
    public StockResponseDTO getStockWithProfiles(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado con id: " + stockId));

        AccountStockDTO accountSummary = new AccountStockDTO(
                stock.getAccountId().getId(),
                stock.getAccountId().getServiceName(),
                stock.getAccountId().getSvgUrl()
        );

        List<ProfileDTO> profileDTOs = stock.getProfiles() != null ?
                stock.getProfiles().stream()
                        .map(profile -> new ProfileDTO(profile.getProfileName(), profile.getProfilePassword()))
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        return new StockResponseDTO(
                stock.getId(),
                accountSummary,
                stock.getType().name(),
                stock.getEmail(),
                stock.getPassword(),
                stock.getIsAssigned(),
                profileDTOs
        );
    }
}