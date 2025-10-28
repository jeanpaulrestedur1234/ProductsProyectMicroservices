package com.example.demo.application.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryDTO {
    private Long id;
    private String name;
    private String sku;
    private Double price;
    private Integer quantity;
    private String description;

    
}
