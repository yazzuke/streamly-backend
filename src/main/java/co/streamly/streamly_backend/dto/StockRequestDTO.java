package co.streamly.streamly_backend.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestDTO {
    private Long accountId;
    private String type;
    private String email;
    private String password;
    private List<ProfileDTO> profiles;
}
