package com.example.demo.infrastructure.controller;

import com.example.demo.application.dto.ProductInventoryDTO;
import com.example.demo.application.service.InventoryService;
import com.example.demo.domain.model.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventory-microservice/inventories")
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Inventory> create(@RequestParam Long productId,
            @RequestParam Integer quantity) {
        log.info("POST /inventories - productId={}, quantity={}", productId, quantity);
        Inventory inventory = service.createInventory(productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getByProductId(@PathVariable Long productId) {
        log.info("GET /inventories/{} - request", productId);
        Inventory inventory = service.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping
    public ResponseEntity<List<ProductInventoryDTO>> getAllProductsWithQuantities() {
        log.info("GET /inventories - fetching all products with quantities");
        List<ProductInventoryDTO> result = service.getAllProductsWithQuantities();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Inventory> updateQuantity(@PathVariable Long productId,
            @RequestParam Integer quantityChange) {
        log.info("PUT /inventories/{} - quantityChange={}", productId, quantityChange);
        Inventory updated = service.updateQuantity(productId, quantityChange);
        return ResponseEntity.ok(updated);
    }
}
