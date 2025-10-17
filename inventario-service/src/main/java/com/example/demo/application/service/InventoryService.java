package com.example.demo.application.service;

import com.example.demo.application.dto.ProductInventoryDTO;
import com.example.demo.application.dto.ProductResponse;
import com.example.demo.domain.model.Inventory;
import com.example.demo.domain.repository.InventoryRepository;
import com.example.demo.infrastructure.exception.InventoryNotFoundException;
import com.example.demo.infrastructure.exception.ProductNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository repository;
    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public InventoryService(
            InventoryRepository repository,
            @Value("${PRODUCT_SERVICE_URL:http://localhost:8081/products}") String productServiceUrl) {
            this.repository = repository;
            this.restTemplate = new RestTemplate();
            this.productServiceUrl = productServiceUrl;
        }

    // Crear inventario
    public Inventory createInventory(Long productId, Integer quantity) {
        log.info("Creating inventory for productId={} quantity={}", productId, quantity);
        // Validar que el producto exista
        validateProductExists(productId);

        Inventory inventory = new Inventory(productId, quantity);
        Inventory saved = repository.save(inventory);
        log.info("Inventory created: {}", saved);
        return saved;
    }

    // Obtener inventario por productId
    public Inventory getInventoryByProductId(Long productId) {
        log.info("Fetching inventory for productId={}", productId);
        return repository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    // Actualizar cantidad de inventario
    public Inventory updateQuantity(Long productId, Integer quantityChange) {
        log.info("Updating inventory for productId={} with change={}", productId, quantityChange);
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));

        inventory.setQuantity(inventory.getQuantity() + quantityChange);
        Inventory updated = repository.save(inventory);

        log.info("Inventory updated: {}", updated);
        log.info("Event: Inventory for product {} changed to {}", productId, updated.getQuantity());
        return updated;
    }

    // Obtener todos los productos con cantidades
    public List<ProductInventoryDTO> getAllProductsWithQuantities() {
        log.info("Fetching all inventories with product details");

        List<Inventory> inventories = repository.findAll();

        return inventories.stream().map(inventory -> {
            ProductResponse product;
            try {
                product = restTemplate.getForObject(
                        productServiceUrl + "/" + inventory.getProductId(),
                        ProductResponse.class
                );
            } catch (Exception e) {
                log.warn("Product ID {} not found in products service", inventory.getProductId());
                // Manejo de producto inexistente de forma segura
                product = new ProductResponse(inventory.getProductId(), "UNKNOWN", "UNKNOWN", 0.0);
            }

            return new ProductInventoryDTO(
                    inventory.getProductId(),
                    product.getName(),
                    product.getSku(),
                    product.getPrice(),
                    inventory.getQuantity()
            );
        }).collect(Collectors.toList());
    }

    // Validación centralizada de existencia de producto
    private void validateProductExists(Long productId) {
        try {
            restTemplate.getForObject(productServiceUrl + "/" + productId, Object.class);
        } catch (Exception e) {
            log.warn("Product ID {} not found in products service", productId);
            throw new ProductNotFoundException(productId);
        }
    }
}
