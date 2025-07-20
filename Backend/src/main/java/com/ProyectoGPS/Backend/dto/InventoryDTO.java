package com.ProyectoGPS.Backend.dto;

import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.model.Product;

import java.util.Date;

public class InventoryDTO {
    private Long id;
    private Long warehouseId;
    private Long batchId;
    private Integer quantity;
    private Integer currentStock;
    private String inventoryType;
    private Date lastUpdate;
    
    // Objetos anidados para responses completas
    private WarehouseDTO warehouse;
    private BatchDTO batch;

    // Constructor vacío
    public InventoryDTO() {}

    // Constructor que convierte desde Inventory (para responses)
    public InventoryDTO(Inventory inventory) {
        this.id = inventory.getId();
        this.quantity = inventory.getQuantity();
        this.currentStock = inventory.getCurrentStock();
        this.inventoryType = inventory.getInventoryType();
        this.lastUpdate = inventory.getLastUpdate();
        
        if (inventory.getWarehouse() != null) {
            this.warehouseId = inventory.getWarehouse().getId();
            this.warehouse = new WarehouseDTO(inventory.getWarehouse());
        }
        
        if (inventory.getBatch() != null) {
            this.batchId = inventory.getBatch().getId();
            this.batch = new BatchDTO(inventory.getBatch());
        }
    }

    // DTOs anidados
    public static class WarehouseDTO {
        private Long id;
        private String name;
        private String location;

        public WarehouseDTO() {}

        public WarehouseDTO(Warehouse warehouse) {
            this.id = warehouse.getId();
            this.name = warehouse.getName();
            this.location = warehouse.getLocation();
        }

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }

    public static class BatchDTO {
        private Long id;
        private String batchNumber;
        private Date expirationDate;
        private ProductDTO product;
        private Long productId;

        public BatchDTO() {}

        public BatchDTO(Batch batch) {
            this.id = batch.getId();
            this.batchNumber = batch.getBatchNumber();
            this.expirationDate = batch.getExpirationDate();
            
            if (batch.getProduct() != null) {
                this.product = new ProductDTO(batch.getProduct());
                this.productId = batch.getProduct().getId();
            }
        }

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getBatchNumber() { return batchNumber; }
        public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
        public Date getExpirationDate() { return expirationDate; }
        public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
        public ProductDTO getProduct() { return product; }
        public void setProduct(ProductDTO product) { this.product = product; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
    }

    public static class ProductDTO {
        private Long id;
        private String code;
        private String name;
        private String description;
        private String category;
        private String presentation;
        private String laboratory;

        public ProductDTO() {}

        public ProductDTO(Product product) {
            this.id = product.getId();
            this.code = product.getCode();
            this.name = product.getName();
            this.description = product.getDescription();
            this.category = product.getCategory();
            this.presentation = product.getPresentation();
            this.laboratory = product.getLaboratory();
        }

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getPresentation() { return presentation; }
        public void setPresentation(String presentation) { this.presentation = presentation; }
        public String getLaboratory() { return laboratory; }
        public void setLaboratory(String laboratory) { this.laboratory = laboratory; }
    }

    // Getters y Setters principales
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

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

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }

    public BatchDTO getBatch() {
        return batch;
    }

    public void setBatch(BatchDTO batch) {
        this.batch = batch;
    }
}
