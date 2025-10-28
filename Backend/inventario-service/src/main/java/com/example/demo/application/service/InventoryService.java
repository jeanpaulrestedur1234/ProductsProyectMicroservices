package com.example.demo.application.service;

import com.example.demo.application.dto.ProductInventoryDTO;
import com.example.demo.application.dto.ProductInventoryResponseDTO;
import com.example.demo.application.dto.ProductResponse;
import com.example.demo.domain.model.Inventory;
import com.example.demo.domain.repository.InventoryRepository;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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
        this.productServiceUrl = productServiceUrl;

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        this.restTemplate = new RestTemplate(requestFactory);
    }

    /**
     * 🔹 Obtener todos los productos con la cantidad real desde la base de datos local
     */
    public List<ProductInventoryResponseDTO> getAllProductsWithQuantities() {
        log.info("Fetching all products with inventory quantities from DB...");

        try {
            // Obtener todos los productos del microservicio de productos
            ResponseEntity<ProductResponse[]> response = restTemplate.getForEntity(
                    productServiceUrl, ProductResponse[].class);

            List<ProductResponse> products = Arrays.asList(Objects.requireNonNull(response.getBody()));

            // Obtener inventario local (productId -> cantidad)
            Map<Long, Integer> inventoryMap = repository.findAll().stream()
                    .collect(Collectors.toMap(
                            Inventory::getProductId,
                            Inventory::getQuantity,
                            (a, b) -> b));

            // Combinar datos
            return products.stream()
                    .map(product -> new ProductInventoryResponseDTO(
                            product.getId(),
                            product.getName(),
                            product.getSku(),
                            product.getPrice(),
                            inventoryMap.getOrDefault(product.getId(), 0),
                            product.getDescription()
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching combined product-inventory data: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener la lista de productos con inventario local.");
        }
    }

    /**
     * 🔹 Obtener un producto específico con su inventario local
     */
    public ProductInventoryResponseDTO getInventoryByProductId(Long productId) {
        log.info("Fetching inventory for productId={}", productId);

        ProductResponse product = getProductById(productId);
        Inventory inventory = repository.findByProductId(productId)
                .orElseGet(() -> new Inventory(null, productId, 0));

        return new ProductInventoryResponseDTO(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                inventory.getQuantity(),
                product.getDescription()
        );
    }

    /**
     * 🔹 Actualizar (sumar o restar) cantidad del inventario local
     */
    public ProductInventoryDTO updateQuantity(Long productId, Integer quantityChange) {
        log.info("Updating local inventory for productId={} with change={}", productId, quantityChange);

        ProductResponse product = getProductById(productId);

        Inventory inventory = repository.findByProductId(productId)
                .orElseGet(() -> new Inventory(null, productId, 0));

        int newQuantity = Math.max(0, inventory.getQuantity() + quantityChange);
        inventory.setQuantity(newQuantity);
        repository.save(inventory);

        log.info("Inventory for productId={} updated successfully. New quantity={}", productId, newQuantity);

        return new ProductInventoryDTO(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                newQuantity,
                product.getDescription()
        );
    }

    /**
     * 🔹 Eliminar inventario local y producto del microservicio remoto
     */
    public void deleteInventory(Long productId) {
        log.info("Deleting inventory and remote product for productId={}", productId);

        getProductById(productId);

        repository.findByProductId(productId).ifPresent(repository::delete);

        try {
            restTemplate.delete(productServiceUrl + "/" + productId);
            log.info("Product {} deleted successfully from remote service and local DB", productId);
        } catch (Exception e) {
            log.error("Error deleting remote product: {}", e.getMessage());
            throw new RuntimeException("Error eliminando el producto remoto: " + e.getMessage());
        }
    }

    /**
     * 🔹 Registrar una compra: disminuir la cantidad del inventario local
     */
    public ProductInventoryDTO purchaseProduct(Long id, Integer quantityToBuy) {
        log.info("Processing purchase for productId={} with quantity={}", id, quantityToBuy);

        ProductResponse product = getProductById(id);
        Inventory inventory = repository.findByProductId(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (inventory.getQuantity() < quantityToBuy) {
            throw new RuntimeException("No hay suficiente inventario disponible para realizar la compra.");
        }

        inventory.setQuantity(inventory.getQuantity() - quantityToBuy);
        repository.save(inventory);

        log.info("Purchase completed for productId={}. Remaining quantity={}", id, inventory.getQuantity());

        return new ProductInventoryDTO(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                inventory.getQuantity(),
                product.getDescription()
        );
    }

    /**
     * 🔹 Obtener producto desde el microservicio remoto
     */
    private ProductResponse getProductById(Long productId) {
        try {
            ProductResponse product = restTemplate.getForObject(
                    productServiceUrl + "/" + productId, ProductResponse.class);
            if (product == null)
                throw new ProductNotFoundException(productId);
            return product;
        } catch (Exception e) {
            log.warn("Product ID {} not found in remote service", productId);
            throw new ProductNotFoundException(productId);
        }
    }
}
