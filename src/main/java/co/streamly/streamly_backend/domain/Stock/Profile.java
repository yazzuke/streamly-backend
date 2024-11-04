package co.streamly.streamly_backend.domain.Stock;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.streamly.streamly_backend.domain.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "profiles")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    @JsonBackReference
    private Stock stock;

    @Column(name = "profile_name", nullable = false, length = 255)
    private String profileName;

    @Column(name = "profile_password", nullable = false, length = 255)
    private String profilePassword;

    @Column(name = "is_assigned", nullable = false)
    private Boolean isAssigned = false;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

}