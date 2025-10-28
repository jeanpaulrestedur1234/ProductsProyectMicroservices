package com.example.demo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 🔹 Esto es lo que activa el autoincremento
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String sku;

    private Double price;
    private String description;
}
