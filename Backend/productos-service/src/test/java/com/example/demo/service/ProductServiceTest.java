package com.example.demo.service;

import com.example.demo.application.service.ProductService;
import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.infrastructure.exception.InvalidProductException;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testCreateValidProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setSku("SKU-123");
        product.setDescription("High-end laptop");

        Product saved = service.save(product);

        assertNotNull(saved.getId());
        assertEquals("Laptop", saved.getName());
        assertEquals(1500.0, saved.getPrice());
    }

    @Test
    void testCreateInvalidProduct() {
        Product invalid = new Product();
        invalid.setPrice(null); // faltan campos obligatorios

        InvalidProductException ex = assertThrows(InvalidProductException.class, () -> {
            service.save(invalid);
        });

        assertEquals("Name and price must not be null", ex.getMessage());
    }

    @Test
    void testUpdateExistingProduct() {
        Product product = new Product();
        product.setName("Tablet");
        product.setPrice(800.0);
        product = service.save(product);

        Product updated = new Product();
        updated.setName("Tablet Pro");
        updated.setPrice(1000.0);

        Product result = service.update(product.getId(), updated);

        assertEquals("Tablet Pro", result.getName());
        assertEquals(1000.0, result.getPrice());
    }

    @Test
    void testUpdateNonExistingProduct() {
        Long id = 888L;
        Product updated = new Product();
        updated.setName("Test");
        updated.setPrice(10.0);

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> {
            service.update(id, updated);
        });

        assertEquals("Product with ID 888 not found", ex.getMessage());
    }

    @Test
    void testDeleteExistingProduct() {
        Product product = new Product();
        product.setName("Mouse");
        product.setPrice(20.0);
        product = service.save(product);

        service.deleteById(product.getId());

        assertFalse(repository.findById(product.getId()).isPresent());
    }

    @Test
    void testDeleteNonExistingProduct() {
        Long id = 999L;

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> {
            service.deleteById(id);
        });

        assertEquals("Product with ID 999 not found", ex.getMessage());
    }


}
