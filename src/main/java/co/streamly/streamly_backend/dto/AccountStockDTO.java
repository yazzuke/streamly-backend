package co.streamly.streamly_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStockDTO {
    private Long id;           // Agregar el campo id
    private String serviceName;
    private String imageUrl;
    private String svgUrl;

  
}
