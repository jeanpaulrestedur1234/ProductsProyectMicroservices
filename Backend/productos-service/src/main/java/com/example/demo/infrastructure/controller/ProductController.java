package com.example.demo.infrastructure.controller;

import com.example.demo.application.service.ProductService;
import com.example.demo.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        log.info("POST /products - request: {}", product);
        Product saved = service.save(product);
        log.info("POST /products - response: {}", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        log.info("GET /products/{} - request", id);
        Product product = service.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product ID {} not found", id);
                    return new com.example.demo.infrastructure.exception.ProductNotFoundException(id);
                });
        log.info("GET /products/{} - response: {}", id, product);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /products - page: {}, size: {}", page, size);
        List<Product> products = service.findAll(page, size);
        log.info("GET /products - returning {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product updated) {
        log.info("PUT /products/{} - request: {}", id, updated);
        Product product = service.update(id, updated);
        log.info("PUT /products/{} - updated successfully: {}", id, product);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /products/{} - request", id);
        service.deleteById(id);
        log.info("DELETE /products/{} - deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
