package com.example.demo.infrastructure.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(Long inventoryId) {
        super("Inventory not found with id: " + inventoryId);
    }
}
