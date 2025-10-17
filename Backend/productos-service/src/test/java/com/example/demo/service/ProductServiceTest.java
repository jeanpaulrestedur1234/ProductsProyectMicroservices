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
    void testDeleteNonExistingProduct() {
        Long id = 999L;
        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> {
            service.deleteById(id);
        });
        assertEquals("Product with ID 999 not found", ex.getMessage());
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
    void testCreateInvalidProduct() {
        Product invalid = new Product();
        invalid.setPrice(null); // name obligatorio
        InvalidProductException ex = assertThrows(InvalidProductException.class, () -> {
            service.save(invalid);
        });
        assertEquals("Name and price must not be null", ex.getMessage());
    }
}
