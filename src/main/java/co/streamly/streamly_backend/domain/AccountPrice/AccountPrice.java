package co.streamly.streamly_backend.domain.AccountPrice;
import co.streamly.streamly_backend.domain.Account.Account;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "months", nullable = false)
    private int months;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeAccount type;

    @Column(name = "price", nullable = false)
    private double price;
}