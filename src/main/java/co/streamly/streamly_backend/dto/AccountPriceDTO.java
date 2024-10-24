package co.streamly.streamly_backend.dto;
import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.AccountPrice.AccountPrice;
import co.streamly.streamly_backend.domain.AccountPrice.TypeAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountPriceDTO {

    private Long id;
    private int months;
    private double price;
    private Long accountId;
    private String serviceName;
    private TypeAccount type;


    // Constructor personalizado que recibe una entidad AccountPrice
    public AccountPriceDTO(AccountPrice accountPrice) {
        this.id = accountPrice.getId();
        this.months = accountPrice.getMonths();
        this.price = accountPrice.getPrice();
        Account account = accountPrice.getAccount();
        if (account != null) {
            this.accountId = account.getId();
            this.serviceName = account.getServiceName();
        }
        this.type = accountPrice.getType();
    }
}

