package com.example.demo.application.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryResponseDTO {
    public ProductInventoryResponseDTO(Long id2, String name2, String sku2, Double price2, int i, String description2) {
        this.id = id2;
        this.name = name2;
        this.sku = sku2;
        this.price = price2;
        this.quantity = i;
        this.description = description2;
    }
    private Long id;
    private String name;
    private String sku;
    private String description;
    private Double price;
    private Integer quantity;

}
