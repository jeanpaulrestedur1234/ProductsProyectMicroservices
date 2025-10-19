package com.example.demo.infrastructure.controller;

import com.example.demo.application.dto.ProductInventoryDTO;
import com.example.demo.application.service.InventoryService;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
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
    public ResponseEntity<?> getAllProductsWithQuantities() {
        log.info("GET /inventories - fetching all products with quantities");
        try {
            List<ProductInventoryDTO> result = service.getAllProductsWithQuantities();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error fetching all products: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching all products: " + e.getMessage());
        }
    }

    /**
     * 🔹 Obtener un producto específico por ID con su cantidad.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> getByProductId(@PathVariable Long productId) {
        log.info("GET /inventories/{} - request", productId);
        try {
            ProductInventoryDTO result = service.getInventoryByProductId(productId);
            return ResponseEntity.ok(result);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + productId);
        } catch (Exception e) {
            log.error("Error fetching product {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching product: " + e.getMessage());
        }
    }

    /**
     * 🔹 Actualizar la cantidad (quantity) de un producto.
     */
    @PatchMapping("/{productId}/quantity")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantityChange) {
        log.info("PATCH /inventories/{}/quantity - quantityChange={}", productId, quantityChange);
        try {
            ProductInventoryDTO updated = service.updateQuantity(productId, quantityChange);
            return ResponseEntity.ok(updated);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + productId);
        } catch (Exception e) {
            log.error("Error updating quantity for product {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating quantity: " + e.getMessage());
        }
    }

    /**
     * 🔹 Registrar una compra de un producto (disminuir quantity).
     */
    @PostMapping("/{productId}/purchase")
    public ResponseEntity<?> purchaseProduct(
            @PathVariable Long productId,
            @RequestParam Integer quantityToBuy) {
        log.info("POST /inventories/{}/purchase - quantityToBuy={}", productId, quantityToBuy);
        try {
            ProductInventoryDTO updated = service.purchaseProduct(productId, quantityToBuy);
            return ResponseEntity.ok(updated);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + productId);
        } catch (Exception e) {
            log.error("Error purchasing product {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error purchasing product: " + e.getMessage());
        }
    }
}
