package com.example.demo.application.service;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import com.example.demo.infrastructure.exception.InvalidProductException;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product save(Product product) {
        if (product.getName() == null || product.getPrice() == null) {
            throw new InvalidProductException("Name and price must not be null");
        }
        return repository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public Product update(Long id, Product updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setSku(updated.getSku());
                    existing.setPrice(updated.getPrice());
                    existing.setDescription(updated.getDescription());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public List<Product> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }
}
