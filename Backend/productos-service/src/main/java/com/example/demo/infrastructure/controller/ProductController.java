package com.example.demo.infrastructure.controller;

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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequestDTO requestDTO) {
        try {
            log.info("Creating new product: {}", requestDTO);
            Product product = mapToEntity(requestDTO);
            Product saved = service.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(saved));
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el producto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            log.info("Fetching product with ID: {}", id);
            Product product = service.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
            return ResponseEntity.ok(mapToDTO(product));
        } catch (ProductNotFoundException e) {
            log.warn("Product not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error retrieving product with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el producto: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductRequestDTO requestDTO) {
        try {
            log.info("Updating product with ID: {}", id);
            Product updated = service.update(id, mapToEntity(requestDTO));
            return ResponseEntity.ok(mapToDTO(updated));
        } catch (ProductNotFoundException e) {
            log.warn("Cannot update. Product not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el producto con ID: " + id);
        } catch (Exception e) {
            log.error("Error updating product with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el producto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            log.info("Deleting product with ID: {}", id);
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            log.warn("Cannot delete. Product not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el producto con ID: " + id);
        } catch (Exception e) {
            log.error("Error deleting product with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el producto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            log.info("Listing products (page: {}, size: {})", page, size);
            List<ProductResponseDTO> response = service.findAll(page, size)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error listing products: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar los productos: " + e.getMessage());
        }
    }

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
