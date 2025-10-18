package com.example.demo.infrastructure.controller;

import com.example.demo.application.dto.ProductInventoryDTO;
import com.example.demo.application.service.InventoryService;
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

    /**
     * 🔹 Obtener todos los productos con sus cantidades.
     */
    @GetMapping
    public ResponseEntity<List<ProductInventoryDTO>> getAllProductsWithQuantities() {
        log.info("GET /inventories - fetching all products with quantities");
        List<ProductInventoryDTO> result = service.getAllProductsWithQuantities();
        return ResponseEntity.ok(result);
    }

    /**
     * 🔹 Obtener un producto específico por ID con su cantidad.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductInventoryDTO> getByProductId(@PathVariable Long productId) {
        log.info("GET /inventories/{} - request", productId);
        ProductInventoryDTO result = service.getInventoryByProductId(productId);
        return ResponseEntity.ok(result);
    }

    /**
     * 🔹 Actualizar la cantidad (quantity) de un producto.
     */
    @PatchMapping("/{productId}/quantity")
    public ResponseEntity<ProductInventoryDTO> updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantityChange) {
        log.info("PATCH /inventories/{}/quantity - quantityChange={}", productId, quantityChange);
        ProductInventoryDTO updated = service.updateQuantity(productId, quantityChange);
        return ResponseEntity.ok(updated);
    }
}
