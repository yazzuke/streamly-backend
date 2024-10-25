package co.streamly.streamly_backend.controller;

import co.streamly.streamly_backend.domain.Combo.Combo;
import co.streamly.streamly_backend.domain.ComboPrice.ComboPrice;
import co.streamly.streamly_backend.domain.ComboPrice.TypeCombo;
import co.streamly.streamly_backend.dto.ComboPriceDTO;
import co.streamly.streamly_backend.dto.ComboSummaryDTO;
import co.streamly.streamly_backend.service.ComboService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/streamly/combos")
public class ComboController {

    private final ComboService comboService;

    @Autowired
    public ComboController(ComboService comboService) {
        this.comboService = comboService;
    }

    // Endpoint para crear un nuevo combo
    @PostMapping
    public ResponseEntity<Combo> createCombo(@RequestBody Combo combo) {
        Combo createdCombo = comboService.createCombo(combo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCombo);
    }

    // Endpoint para obtener todos los combos con el precio más bajo
    @GetMapping
    public ResponseEntity<List<ComboSummaryDTO>> getAllCombos() {
        List<ComboSummaryDTO> combos = comboService.getAllCombosSummaryWithLowestPrice();
        return ResponseEntity.ok(combos);
    }

    // Endpoint para obtener el detalle de un combo específico
    @GetMapping("/{id}")
    public ResponseEntity<ComboSummaryDTO> getComboById(@PathVariable Long id) {
        Optional<ComboSummaryDTO> combo = comboService.getComboById(id);
        return combo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para crear un precio para un combo
    @PostMapping("/{comboId}/prices")
    public ResponseEntity<ComboPriceDTO> createComboPrice(@PathVariable Long comboId,
            @RequestBody ComboPrice comboPrice) {
        ComboPriceDTO createdPrice = comboService.createComboPrice(comboId, comboPrice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrice);
    }

    // Endpoint para obtener todos los precios de un combo específico
    @GetMapping("/{comboId}/prices")
    public ResponseEntity<List<ComboPriceDTO>> getPricesByComboId(@PathVariable Long comboId) {
        List<ComboPriceDTO> prices = comboService.getPricesByComboId(comboId);
        return ResponseEntity.ok(prices);
    }

    // Endpoint para actualizar un combo específico
    @PutMapping("/{id}")
    public ResponseEntity<Combo> updateCombo(@PathVariable Long id, @RequestBody Combo comboDetails) {
        Optional<Combo> updatedCombo = comboService.updateCombo(id, comboDetails);
        return updatedCombo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para eliminar un combo por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCombo(@PathVariable Long id) {
        boolean deleted = comboService.deleteCombo(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/combos/{comboId}/price")
    public ResponseEntity<ComboPriceDTO> getComboPrice(
            @PathVariable Long comboId,
            @RequestParam int months,
            @RequestParam TypeCombo type) {
        Optional<ComboPriceDTO> price = comboService.getComboPrice(comboId, months, type);
        return price.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    

}
