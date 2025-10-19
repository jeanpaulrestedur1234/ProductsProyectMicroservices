package com.example.demo.infrastructure.controller;

import com.example.demo.application.dto.QuantityUpdateRequest;
import com.example.demo.application.service.ProductService;
import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.exception.ProductNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-microservice/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    /**
     * 🔹 Crear un nuevo producto
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product) {
        log.info("POST /products - request: {}", product);
        try {
            Product saved = service.save(product);
            log.info("POST /products - response: {}", saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating product: " + e.getMessage());
        }
    }

    /**
     * 🔹 Obtener un producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("GET /products/{} - request", id);
        try {
            Product product = service.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
            log.info("GET /products/{} - response: {}", id, product);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + id);
        } catch (Exception e) {
            log.error("Error fetching product {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching product: " + e.getMessage());
        }
    }

    /**
     * 🔹 Listar productos con paginación
     */
    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /products - page: {}, size: {}", page, size);
        try {
            List<Product> products = service.findAll(page, size);
            log.info("GET /products - returning {} products", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error listing products: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error listing products: " + e.getMessage());
        }
    }

    /**
     * 🔹 Actualizar un producto existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product updated) {
        log.info("PUT /products/{} - request: {}", id, updated);
        try {
            Product product = service.update(id, updated);
            log.info("PUT /products/{} - updated successfully: {}", id, product);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + id);
        } catch (Exception e) {
            log.error("Error updating product {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating product: " + e.getMessage());
        }
    }

    /**
     * 🔹 Eliminar un producto por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("DELETE /products/{} - request", id);
        try {
            service.deleteById(id);
            log.info("DELETE /products/{} - deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + id);
        } catch (Exception e) {
            log.error("Error deleting product {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting product: " + e.getMessage());
        }
    }

    /**
     * 🔹 Actualizar la cantidad (quantity) de un producto
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long id,
            @RequestBody QuantityUpdateRequest request) {
        Integer quantity = request.getQuantity();
        log.info("PATCH /products/{}/quantity - request: {}", id, quantity);

        try {
            Integer updatedQuantity = service.updateQuantity(id, quantity);
            log.info("PATCH /products/{}/quantity - updated successfully: {}", id, updatedQuantity);
            return ResponseEntity.ok(updatedQuantity);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + id);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid quantity update for product {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid quantity update: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error updating quantity for product {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating quantity: " + e.getMessage());
        }
    }

    /**
     * 🔹 Registrar compra (disminuye cantidad)
     */
    @PostMapping("/{id}/purchase")
    public ResponseEntity<?> purchaseProduct(
            @PathVariable Long id,
            @RequestBody QuantityUpdateRequest request) {
        Integer purchaseQuantity = request.getQuantity();
        log.info("POST /products/{}/purchase - requested quantity: {}", id, purchaseQuantity);

        try {
            Integer updatedQuantity = service.purchaseProduct(id, purchaseQuantity);
            log.info("POST /products/{}/purchase - purchase successful, remaining quantity: {}", id, updatedQuantity);
            return ResponseEntity.ok(updatedQuantity);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found: " + id);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid purchase for product {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid purchase: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error purchasing product {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error purchasing product: " + e.getMessage());
        }
    }
}
