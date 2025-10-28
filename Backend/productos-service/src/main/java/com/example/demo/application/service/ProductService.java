package com.example.demo.application.service;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.infrastructure.exception.InvalidProductException;
import com.example.demo.infrastructure.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    /** 🔹 Crear un nuevo producto */
    public Product save(Product product) {
        log.info("Attempting to create product: {}", product);

        if (product.getName() == null || product.getPrice() == null) {
            log.warn("Invalid product data: {}", product);
            throw new InvalidProductException("El nombre y el precio no pueden ser nulos");
        }

        Product saved = repository.save(product);
        log.info("Product created with ID: {}", saved.getId());
        return saved;
    }

    /** 🔹 Obtener un producto por ID */
    public Optional<Product> findById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return repository.findById(id);
    }

    /** 🔹 Actualizar un producto existente */
    public Product update(Long id, Product productData) {
        log.info("Updating product ID: {} with data: {}", id, productData);

        Product existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (productData.getName() != null)
            existing.setName(productData.getName());
        if (productData.getSku() != null)
            existing.setSku(productData.getSku());
        if (productData.getDescription() != null)
            existing.setDescription(productData.getDescription());
        if (productData.getPrice() != null)
            existing.setPrice(productData.getPrice());

        Product updated = repository.save(existing);
        log.info("Product updated successfully: {}", updated);
        return updated;
    }

    /** 🔹 Eliminar un producto por ID */
    public void deleteById(Long id) {
        log.info("Deleting product ID: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Product ID {} not found for deletion", id);
            throw new ProductNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Product ID {} deleted successfully", id);
    }

    /** 🔹 Listar productos con paginación */
    public List<Product> findAll(int page, int size) {
        log.info("Listing products, page: {}, size: {}", page, size);
        List<Product> products = repository.findAll(PageRequest.of(page, size)).getContent();
        log.info("Found {} products", products.size());
        return products;
    }
}
