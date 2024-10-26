package co.streamly.streamly_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWrapper {
    private String type; // Es "account" o "combo"
    private AccountAdminDTO account;
    private ComboAdminDTO combo;
}
