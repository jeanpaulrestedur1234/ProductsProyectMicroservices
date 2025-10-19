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
        log.debug("Validating product ID: {}", product.getId() != null ? product.getId() : "new product");
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

    public Product update(Long id, Product updatedProduct) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (updatedProduct.getName() != null)
            existing.setName(updatedProduct.getName());
        if (updatedProduct.getSku() != null)
            existing.setSku(updatedProduct.getSku());
        if (updatedProduct.getDescription() != null)
            existing.setDescription(updatedProduct.getDescription());
        if (updatedProduct.getPrice() != null)
            existing.setPrice(updatedProduct.getPrice());
        if (updatedProduct.getQuantity() != null)
            existing.setQuantity(updatedProduct.getQuantity());

        return repository.save(existing);
    }

    public Integer purchaseProduct(Long productId, Integer quantityToBuy) {
        Product product = findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.getQuantity() < quantityToBuy) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        product.setQuantity(product.getQuantity() - quantityToBuy);
        save(product);

        return product.getQuantity();
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
    public Integer updateQuantity(Long id, Integer quantity) {
        log.info("Updating quantity for product ID: {} to {}", id, quantity);

        if (quantity == null || quantity < 0) {
            log.warn("Invalid quantity value: {}. Quantity cannot be negative or null.", quantity);
            throw new IllegalArgumentException("Quantity cannot be negative or null");
        }

        return repository.findById(id)
                .map(existing -> {
                    existing.setQuantity(quantity);
                    repository.save(existing);
                    log.info("Product quantity updated: {}", existing);
                    return existing.getQuantity();
                })
                .orElseThrow(() -> {
                    log.warn("Product ID {} not found for quantity update", id);
                    return new ProductNotFoundException(id);
                });
    }

}
