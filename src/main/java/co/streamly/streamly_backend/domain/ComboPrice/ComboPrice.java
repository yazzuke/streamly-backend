package co.streamly.streamly_backend.domain.ComboPrice;

import co.streamly.streamly_backend.domain.Combo.Combo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "combo_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    @Column(name = "months", nullable = false)
    private int months;

    @Column(name = "price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeCombo type;  
}
