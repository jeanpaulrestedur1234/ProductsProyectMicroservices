package com.example.demo.controller;

import com.example.demo.application.dto.ProductRequestDTO;
import com.example.demo.application.dto.ProductResponseDTO;
import com.example.demo.application.service.ProductService;
import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-microservice/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO requestDTO) {
        log.info("Creating new product: {}", requestDTO);
        Product product = mapToEntity(requestDTO);
        Product saved = service.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        Product product = service.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ResponseEntity.ok(mapToDTO(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO requestDTO) {

        log.info("Updating product ID: {}", id);
        Product updated = service.update(id, mapToEntity(requestDTO));
        return ResponseEntity.ok(mapToDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting product ID: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ProductResponseDTO> response = service.findAll(page, size)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ---------- Mappers ----------
    private Product mapToEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return product;
    }

    private ProductResponseDTO mapToDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
