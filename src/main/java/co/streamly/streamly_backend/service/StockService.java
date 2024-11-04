package co.streamly.streamly_backend.service;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.Account.AccountRepository;
import co.streamly.streamly_backend.domain.Stock.Profile;
import co.streamly.streamly_backend.domain.Stock.ProfileRepository;
import co.streamly.streamly_backend.domain.Stock.Stock;
import co.streamly.streamly_backend.domain.Stock.StockRepository;
import co.streamly.streamly_backend.dto.StockRequestDTO;
import co.streamly.streamly_backend.dto.StockResponseDTO;
import co.streamly.streamly_backend.dto.AccountStockDTO;
import co.streamly.streamly_backend.dto.ProfileDTO;
import co.streamly.streamly_backend.domain.AccountPrice.TypeAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        // Busca el objeto Account usando el ID proporcionado en stockRequestDTO
        Account account = accountRepository.findById(stockRequestDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account no encontrada con id: " + stockRequestDTO.getAccountId()));
    
        Stock stock = new Stock();
        stock.setAccountId(account);
        stock.setType(TypeAccount.valueOf(stockRequestDTO.getType()));
        stock.setEmail(stockRequestDTO.getEmail());
        stock.setPassword(stockRequestDTO.getPassword());
        stock.setIsAssigned(false);
    
        Stock savedStock = stockRepository.save(stock);
    
        if (stock.getType() == TypeAccount.Pantalla) {
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
        }

        // Construimos y devolvemos el StockResponseDTO a partir del Stock guardado
        AccountStockDTO accountSummary = new AccountStockDTO(
                savedStock.getAccountId().getId(),
                savedStock.getAccountId().getServiceName(),
                savedStock.getAccountId().getImageUrl(),
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

    // Método para obtener todos los registros de stock y convertirlos en StockResponseDTO
    public List<StockResponseDTO> getAllStocks() {
        return stockRepository.findAll().stream().map(stock -> {
            AccountStockDTO accountSummary = new AccountStockDTO(
                    stock.getAccountId().getId(),
                    stock.getAccountId().getServiceName(),
                    stock.getAccountId().getImageUrl(),
                    stock.getAccountId().getSvgUrl()
            );

            List<ProfileDTO> profiles = stock.getProfiles().stream()
                    .map(profile -> new ProfileDTO(profile.getProfileName(), profile.getProfilePassword()))
                    .collect(Collectors.toList());

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

    // Obtener un stock específico por ID y devolver como StockResponseDTO
    public StockResponseDTO getStockWithProfiles(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado con id: " + stockId));

        AccountStockDTO accountSummary = new AccountStockDTO(
                stock.getAccountId().getId(),
                stock.getAccountId().getServiceName(),
                stock.getAccountId().getImageUrl(),
                stock.getAccountId().getSvgUrl()
        );

        List<ProfileDTO> profileDTOs = stock.getProfiles().stream()
                .map(profile -> new ProfileDTO(profile.getProfileName(), profile.getProfilePassword()))
                .collect(Collectors.toList());

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
