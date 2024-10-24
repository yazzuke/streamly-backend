package co.streamly.streamly_backend.dto;

import co.streamly.streamly_backend.domain.Account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryDTO {
    private Long id;
    private String serviceName;
    private String imageUrl;
    private Double lowestPrice;

    public AccountSummaryDTO(Account account, Double lowestPrice) {
        this.id = account.getId();
        this.serviceName = account.getServiceName();
        this.imageUrl = account.getImageUrl();
        this.lowestPrice = lowestPrice;
    }
}