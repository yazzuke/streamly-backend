package co.streamly.streamly_backend.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class StockDTO {
    private Long id;
    private Object accountId; // Puede ser Long (en POST) o AccountStockDTO (en GET)
    private String type;
    private String email;
    private String password;
    private Boolean isAssigned;
    private List<ProfileDTO> profiles;

    // Constructor completo para el GET
    public StockDTO(Long id, AccountStockDTO accountSummary, String type, String email, String password, Boolean isAssigned, List<ProfileDTO> profiles) {
        this.id = id;
        this.accountId = accountSummary;
        this.type = type;
        this.email = email;
        this.password = password;
        this.isAssigned = isAssigned;
        this.profiles = profiles;
    }

    // Constructor para el POST, donde solo usamos el ID de accountId
    public StockDTO(Long accountId, String type, String email, String password, List<ProfileDTO> profiles) {
        this.accountId = accountId;
        this.type = type;
        this.email = email;
        this.password = password;
        this.profiles = profiles;
    }

    // Método para obtener accountId como Long
    public Long getAccountIdAsLong() {
        if (accountId instanceof Long) {
            return (Long) accountId;
        }
        throw new IllegalStateException("Account ID is not of type Long");
    }
}
