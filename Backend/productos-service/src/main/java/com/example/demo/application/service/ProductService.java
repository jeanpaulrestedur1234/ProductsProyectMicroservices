package com.example.demo.application.service;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import com.example.demo.infrastructure.exception.InvalidProductException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product save(Product product) {
        log.info("Attempting to create product: {}", product);
        if (product.getName() == null || product.getPrice() == null) {
            log.warn("Invalid product data: {}", product);
            throw new InvalidProductException("Name and price must not be null");
        }
        Product saved = repository.save(product);
        log.info("Product created with ID: {}", saved.getId());
        return saved;
    }

    public Optional<Product> findById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return repository.findById(id);
    }

    public Product update(Long id, Product updated) {
        log.info("Updating product ID: {} with data: {}", id, updated);
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setSku(updated.getSku());
                    existing.setPrice(updated.getPrice());
                    existing.setDescription(updated.getDescription());
                    Product saved = repository.save(existing);
                    log.info("Product updated: {}", saved);
                    return saved;
                })
                .orElseThrow(() -> {
                    log.warn("Product ID {} not found for update", id);
                    return new ProductNotFoundException(id);
                });
    }

    public void deleteById(Long id) {
        log.info("Deleting product ID: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Product ID {} not found for deletion", id);
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Product ID {} deleted", id);
    }

    public List<Product> findAll(int page, int size) {
        log.info("Listing products, page: {}, size: {}", page, size);
        List<Product> products = repository.findAll(PageRequest.of(page, size)).getContent();
        log.info("Found {} products", products.size());
        return products;
    }
}
