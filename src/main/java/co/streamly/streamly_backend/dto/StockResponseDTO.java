package co.streamly.streamly_backend.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class StockResponseDTO {
    private Long id;
    private AccountStockDTO accountId; // Este es el tipo de accountId en el GET
    private String type;
    private String email;
    private String password;
    private Boolean isAssigned;
    private List<ProfileDTO> profiles;

    // Constructor con todos los parámetros necesarios
    public StockResponseDTO(Long id, AccountStockDTO accountId, String type, String email, String password, Boolean isAssigned, List<ProfileDTO> profiles) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.email = email;
        this.password = password;
        this.isAssigned = isAssigned;
        this.profiles = profiles;
    }
}
