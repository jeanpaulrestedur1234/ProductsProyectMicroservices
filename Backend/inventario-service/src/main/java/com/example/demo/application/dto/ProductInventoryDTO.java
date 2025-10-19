package com.example.demo.application.dto;

public class ProductInventoryDTO {
    private Long productId;
    private String name;
    private String sku;
    private Double price;
    private Integer quantity;
    private String description;

    public ProductInventoryDTO(Long productId, String name, String sku, Double price, Integer quantity, String description) {
        this.productId = productId;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters y setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
