package co.streamly.streamly_backend.dto;

import co.streamly.streamly_backend.domain.Combo.Combo;
import co.streamly.streamly_backend.domain.ComboPrice.ComboPrice;
import co.streamly.streamly_backend.domain.ComboPrice.TypeCombo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboPriceDTO {

    private Long id;
    private int months;
    private double price;
    private Long comboId;
    private String comboName;
    private TypeCombo type;

    public ComboPriceDTO(ComboPrice comboPrice) {
        this.id = comboPrice.getId();
        this.months = comboPrice.getMonths();
        this.price = comboPrice.getPrice();
        this.type = comboPrice.getType();

        Combo combo = comboPrice.getCombo();
        if (combo != null) {
            this.comboId = combo.getId();
            this.comboName = combo.getName();
        }
    }
}
