package com.example.demo.infrastructure.controller;

import com.example.demo.application.dto.ProductInventoryDTO;
import com.example.demo.application.dto.ProductInventoryResponseDTO;
import com.example.demo.application.service.InventoryService;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    /**
     * 🔹 Obtener todos los inventarios (datos combinados del servicio de productos)
     */
    @GetMapping
    public ResponseEntity<List<ProductInventoryResponseDTO>> getAllInventories() {
        log.info("GET /inventories - fetching all inventories");
        List<ProductInventoryResponseDTO> inventories = service.getAllProductsWithQuantities();
        return ResponseEntity.ok(inventories);
    }

    /**
     * 🔹 Obtener inventario por ID (sumando datos del otro servicio)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable Long id) {
        log.info("GET /inventories/{} - fetching single inventory", id);
        try {
            ProductInventoryResponseDTO inventory = service.getInventoryByProductId(id);
            return ResponseEntity.ok(inventory);
        } catch (ProductNotFoundException e) {
            log.warn("Inventory not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Inventario no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error fetching inventory {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al consultar inventario");
        }
    }

    /**
     * 🔹 Actualizar cantidad (sumar o restar)
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantityChange) {
        log.info("PATCH /inventories/{}/quantity - change={}", id, quantityChange);
        try {
            ProductInventoryDTO updated = service.updateQuantity(id, quantityChange);
            return ResponseEntity.ok(updated);
        } catch (ProductNotFoundException e) {
            log.warn("Inventory not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Inventario no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error updating inventory quantity: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar cantidad");
        }
    }

    /**
     * 🔹 Eliminar inventario (primero elimina en el microservicio de productos)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        log.info("DELETE /inventories/{} - deleting inventory", id);
        try {
            service.deleteInventory(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            log.warn("Inventory not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Inventario no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error deleting inventory: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar inventario");
        }
    }

    /**
     * 🔹 Registrar una compra (disminuir cantidad)
     */
    @PostMapping("/{id}/purchase")
    public ResponseEntity<?> purchaseProduct(
            @PathVariable Long id,
            @RequestParam Integer quantityToBuy) {
        log.info("POST /inventories/{}/purchase - quantityToBuy={}", id, quantityToBuy);
        try {
            ProductInventoryDTO updated = service.purchaseProduct(id, quantityToBuy);
            return ResponseEntity.ok(updated);
        } catch (ProductNotFoundException e) {
            log.warn("Inventory not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Inventario no encontrado con ID: " + id);
        } catch (RuntimeException e) {
            log.warn("Error during purchase: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Error purchasing product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la compra");
        }
    }
}
