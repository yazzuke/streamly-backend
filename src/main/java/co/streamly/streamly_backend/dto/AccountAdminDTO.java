package co.streamly.streamly_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import co.streamly.streamly_backend.domain.Account.Account;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountAdminDTO {
    private Long id;
    private String serviceName;
    private String description;
    private String imageUrl;
    private String svgUrl;
    private List<AccountPriceDTO> prices;

    public AccountAdminDTO(Account account, List<AccountPriceDTO> prices) {
        this.id = account.getId();
        this.serviceName = account.getServiceName();
        this.description = account.getDescription();
        this.imageUrl = account.getImageUrl();
        this.svgUrl = account.getSvgUrl();
        this.prices = prices;
    }

}