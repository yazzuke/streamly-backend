package co.streamly.streamly_backend.controller;

import co.streamly.streamly_backend.domain.Account.Account;
import co.streamly.streamly_backend.domain.Combo.Combo;
import co.streamly.streamly_backend.dto.AccountAdminDTO;
import co.streamly.streamly_backend.dto.AccountSummaryDTO;
import co.streamly.streamly_backend.dto.ComboAdminDTO;
import co.streamly.streamly_backend.dto.ComboSummaryDTO;
import co.streamly.streamly_backend.service.ComboService;
import co.streamly.streamly_backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/streamly")
public class ProductController {

    private final AccountService accountService;
    private final ComboService comboService;

    @Autowired
    public ProductController(AccountService accountService, ComboService comboService) {
        this.accountService = accountService;
        this.comboService = comboService;
    }

    @GetMapping("/allproducts")
    public ResponseEntity<List<Object>> getAllProducts() {
        List<AccountSummaryDTO> accounts = accountService.getAllAccountsSummaryWithLowestPrice();
        List<ComboSummaryDTO> combos = comboService.getAllCombosSummaryWithLowestPrice();
        List<Object> allProducts = new ArrayList<>();
        allProducts.addAll(accounts);
        allProducts.addAll(combos);

        return ResponseEntity.ok(allProducts);
    }


     // Endpoint para obtener toda la información para el admin
    @GetMapping("/admin/products")
    public ResponseEntity<List<Object>> getAllProductsForAdmin() {
        List<AccountAdminDTO> accounts = accountService.getAllAccountsForAdmin();
        List<ComboAdminDTO> combos = comboService.getAllCombosForAdmin();

        List<Object> allProducts = new ArrayList<>();
        allProducts.addAll(accounts);
        allProducts.addAll(combos);

        return ResponseEntity.ok(allProducts);
    }
}