package com.example.demo.application.service;

import com.example.demo.application.dto.ProductInventoryDTO;
import com.example.demo.application.dto.ProductResponse;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public InventoryService(
            @Value("${PRODUCT_SERVICE_URL:http://localhost:8081/product-microservice/products}") String productServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.productServiceUrl = productServiceUrl;
    }

    /**
     * 🔹 Obtener todos los productos con sus cantidades (delegando al microservicio de productos).
     */
    public List<ProductInventoryDTO> getAllProductsWithQuantities() {
        log.info("Fetching all products with quantities from Product microservice...");

        try {
            ResponseEntity<ProductResponse[]> response = restTemplate.getForEntity(
                    productServiceUrl,
                    ProductResponse[].class
            );

            List<ProductResponse> products = Arrays.asList(response.getBody());

            return products.stream()
                    .map(product -> new ProductInventoryDTO(
                            product.getId(),
                            product.getName(),
                            product.getSku(),
                            product.getPrice(),
                            product.getQuantity()
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching products from Product service: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener la lista de productos desde el servicio de productos");
        }
    }

    /**
     * 🔹 Obtener la cantidad de inventario (quantity) de un producto por ID.
     */
    public ProductInventoryDTO getInventoryByProductId(Long productId) {
        log.info("Fetching product inventory for productId={}", productId);

        ProductResponse product = getProductById(productId);

        return new ProductInventoryDTO(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                product.getQuantity()
        );
    }

    /**
     * 🔹 Actualizar la cantidad (quantity) de un producto mediante PATCH al servicio de productos.
     */
    public ProductInventoryDTO updateQuantity(Long productId, Integer quantityChange) {
        log.info("Updating quantity for productId={} with change={}", productId, quantityChange);

        ProductResponse product = getProductById(productId);
        Integer newQuantity = product.getQuantity() + quantityChange;

        // PATCH request hacia /product-microservice/products/{id}/quantity
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Integer> request = new HttpEntity<>(newQuantity, headers);

            ResponseEntity<Integer> response = restTemplate.exchange(
                    productServiceUrl + "/" + productId + "/quantity",
                    HttpMethod.PATCH,
                    request,
                    Integer.class
            );

            log.info("Updated quantity for productId={} to {}", productId, response.getBody());
            product.setQuantity(response.getBody());

        } catch (Exception e) {
            log.error("Error updating product quantity for productId={}: {}", productId, e.getMessage());
            throw new RuntimeException("No se pudo actualizar la cantidad del producto");
        }

        return new ProductInventoryDTO(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                product.getQuantity()
        );
    }

    /**
     * 🔹 Validar y obtener producto del microservicio de productos.
     */
    private ProductResponse getProductById(Long productId) {
        try {
            return restTemplate.getForObject(
                    productServiceUrl + "/" + productId,
                    ProductResponse.class
            );
        } catch (Exception e) {
            log.warn("Product ID {} not found in Product service", productId);
            throw new ProductNotFoundException(productId);
        }
    }
}

