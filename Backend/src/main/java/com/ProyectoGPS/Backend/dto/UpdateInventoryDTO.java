package com.ProyectoGPS.Backend.dto;

public class UpdateInventoryDTO {
    private Integer quantity;
    private Integer currentStock;
    
    // Constructors
    public UpdateInventoryDTO() {}
    
    public UpdateInventoryDTO(Integer quantity, Integer currentStock) {
        this.quantity = quantity;
        this.currentStock = currentStock;
    }
    
    // Getters and Setters
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Integer getCurrentStock() {
        return currentStock;
    }
    
    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }
}
