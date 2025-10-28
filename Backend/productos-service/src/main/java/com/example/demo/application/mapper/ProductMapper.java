package com.example.demo.application.mapper;

import com.example.demo.application.dto.ProductRequestDTO;
import com.example.demo.application.dto.ProductResponseDTO;
import com.example.demo.domain.model.Product;

public class ProductMapper {

    private ProductMapper() {
        // Constructor privado para evitar instanciación
    }

    public static Product toEntity(ProductRequestDTO dto) {
        if (dto == null) return null;

        return Product.builder()
                .name(dto.getName())
                .sku(dto.getSku())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();
    }

    public static ProductResponseDTO toDTO(Product product) {
        if (product == null) return null;

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
