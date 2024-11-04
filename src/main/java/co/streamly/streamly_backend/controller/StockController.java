package co.streamly.streamly_backend.controller;

import co.streamly.streamly_backend.dto.StockRequestDTO;
import co.streamly.streamly_backend.dto.StockResponseDTO;
import co.streamly.streamly_backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/streamly/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<StockResponseDTO> createStock(@RequestBody StockRequestDTO stockRequestDTO) {
        // Usa StockRequestDTO para recibir los datos de entrada en el POST
        StockResponseDTO responseDTO = stockService.createStock(stockRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Endpoint para obtener todos los registros de stock en forma de StockResponseDTO
    @GetMapping
    public ResponseEntity<List<StockResponseDTO>> getAllStocks() {
        List<StockResponseDTO> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    // Endpoint para obtener un registro específico de stock por ID
    @GetMapping("/{stockId}")
    public ResponseEntity<StockResponseDTO> getStockById(@PathVariable Long stockId) {
        StockResponseDTO stock = stockService.getStockWithProfiles(stockId);
        return ResponseEntity.ok(stock);
    }
}
