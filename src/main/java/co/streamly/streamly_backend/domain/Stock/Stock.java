package co.streamly.streamly_backend.domain.Stock;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.AccountPrice.TypeAccount;
import co.streamly.streamly_backend.domain.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Table(name = "stock")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAccount type;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "is_assigned", nullable = false)
    private Boolean isAssigned = false;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Profile> profiles;

}
