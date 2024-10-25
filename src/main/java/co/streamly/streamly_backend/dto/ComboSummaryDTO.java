package co.streamly.streamly_backend.dto;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.Combo.Combo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboSummaryDTO {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Double lowestPrice;
    private List<String> accountSvgs; 

  
    public ComboSummaryDTO(Combo combo, Double lowestPrice) {
        this.id = combo.getId();
        this.name = combo.getName();
        this.description = combo.getDescription();
        this.imageUrl = combo.getImageUrl();
        this.lowestPrice = lowestPrice;
        this.accountSvgs = combo.getAccounts().stream()
                               .map(Account::getSvgUrl)  
                               .collect(Collectors.toList());
    }
}
