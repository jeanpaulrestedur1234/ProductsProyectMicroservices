package com.example.demo.infrastructure.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with ID " + id + " not found"); // Cambiado para coincidir con los tests
    }
}

