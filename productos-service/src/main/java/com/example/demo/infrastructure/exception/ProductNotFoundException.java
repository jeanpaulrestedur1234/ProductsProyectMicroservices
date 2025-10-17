package com.example.demo.infrastructure.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("No product found with id " + id);
    }
}

