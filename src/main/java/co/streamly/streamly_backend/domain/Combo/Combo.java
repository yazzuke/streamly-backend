package co.streamly.streamly_backend.domain.Combo;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import co.streamly.streamly_backend.domain.Account.Account;

@Entity
@Table(name = "combo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "image_url")
    private String imageUrl;

    

    @ManyToMany
    @JoinTable(
        name = "combo_account",
        joinColumns = @JoinColumn(name = "combo_id"),
        inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> accounts;
}
