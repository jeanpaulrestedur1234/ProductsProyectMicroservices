package com.example.demo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Builder // Permite crear objetos con el patrón builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String sku;
    private Double price;
    private String description;
}
