package co.streamly.streamly_backend.dto;

import co.streamly.streamly_backend.domain.Combo.Combo;
import co.streamly.streamly_backend.domain.ComboPrice.ComboPrice;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboAdminDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private List<ComboPriceDTO> prices;
    private List<AccountAdminDTO> accounts;

    public ComboAdminDTO(Combo combo, List<ComboPrice> prices, List<AccountAdminDTO> accounts) {
        this.id = combo.getId();
        this.name = combo.getName();
        this.description = combo.getDescription();
        this.imageUrl = combo.getImageUrl();
        this.prices = prices.stream().map(ComboPriceDTO::new).collect(Collectors.toList());
        this.accounts = accounts;  // Pasar directamente los DTOs de cuentas
    }
}
