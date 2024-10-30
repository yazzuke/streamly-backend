package co.streamly.streamly_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManualUserDTO {
    private String email;
    private String password;
    private String displayName;
}