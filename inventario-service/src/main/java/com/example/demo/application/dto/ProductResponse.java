package com.example.demo.application.dto;

public class ProductResponse {

    private Long id;
    private String name;
    private String sku;
    private Double price;

    public ProductResponse() {}

    public ProductResponse(Long id, String name, String sku, Double price) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
