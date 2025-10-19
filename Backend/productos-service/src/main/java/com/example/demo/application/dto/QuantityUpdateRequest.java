package com.example.demo.application.dto;

public class QuantityUpdateRequest {

    private Integer quantity;

    // Constructor sin argumentos (necesario para Jackson)
    public QuantityUpdateRequest() {
    }

    // Constructor opcional con argumentos
    public QuantityUpdateRequest(Integer quantity) {
        this.quantity = quantity;
    }

    // Getter
    public Integer getQuantity() {
        return quantity;
    }

    // Setter
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
