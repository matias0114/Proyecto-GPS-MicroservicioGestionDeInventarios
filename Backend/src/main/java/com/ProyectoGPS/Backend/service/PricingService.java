package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.repository.BatchRepository;
import com.ProyectoGPS.Backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class PricingService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Calcula el precio de un producto basado en el método de pricing configurado
     */
    public BigDecimal calculatePrice(Product product, Warehouse warehouse) {
        if (product.getDefaultPricingMethod() == null) {
            return product.getBasePrice() != null ? product.getBasePrice() : BigDecimal.ZERO;
        }
        
        switch (product.getDefaultPricingMethod()) {
            case LAST_PURCHASE:
                return calculateLastPurchasePrice(product, warehouse);
            case WEIGHTED_AVERAGE:
                return calculateWeightedAveragePrice(product, warehouse);
            case FIFO:
                return calculateFIFOPrice(product, warehouse);
            case LIFO:
                return calculateLIFOPrice(product, warehouse);
            case LILO:
                return calculateLILOPrice(product, warehouse);
            default:
                return product.getBasePrice() != null ? product.getBasePrice() : BigDecimal.ZERO;
        }
    }

    /**
     * Sobrecarga para calcular precio por ID de producto y cantidad
     */
    public BigDecimal calculatePrice(Long productId, Integer quantity) {
        // Para este microservicio, retornamos un precio base
        return BigDecimal.valueOf(1000); // Precio por defecto
    }

    /**
     * Precio de última compra - toma el precio del lote más reciente
     */
    private BigDecimal calculateLastPurchasePrice(Product product, Warehouse warehouse) {
        try {
            List<Batch> batches = batchRepository.findByProductOrderByEntryDateDesc(product);
            if (!batches.isEmpty()) {
                Batch lastBatch = batches.get(0);
                return lastBatch.getPurchasePrice() != null ? lastBatch.getPurchasePrice() : BigDecimal.ZERO;
            }
        } catch (Exception e) {
            // Si hay error, usar precio base
        }
        return product.getBasePrice() != null ? product.getBasePrice() : BigDecimal.ZERO;
    }

    /**
     * Precio promedio ponderado - calcula el promedio basado en cantidades
     */
    private BigDecimal calculateWeightedAveragePrice(Product product, Warehouse warehouse) {
        try {
            List<Inventory> inventories = inventoryRepository.findByWarehouse(warehouse);
            
            BigDecimal totalValue = BigDecimal.ZERO;
            int totalQuantity = 0;

            for (Inventory inventory : inventories) {
                if (inventory.getBatch().getProduct().equals(product) && 
                    inventory.getCurrentStock() != null && inventory.getCurrentStock() > 0 && 
                    inventory.getBatch().getPurchasePrice() != null) {
                    
                    BigDecimal batchValue = inventory.getBatch().getPurchasePrice()
                            .multiply(BigDecimal.valueOf(inventory.getCurrentStock()));
                    totalValue = totalValue.add(batchValue);
                    totalQuantity += inventory.getCurrentStock();
                }
            }

            if (totalQuantity > 0) {
                return totalValue.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            // Si hay error, usar precio base
        }
        return product.getBasePrice() != null ? product.getBasePrice() : BigDecimal.ZERO;
    }

    /**
     * FIFO (First In, First Out) - precio del lote más antiguo
     */
    private BigDecimal calculateFIFOPrice(Product product, Warehouse warehouse) {
        try {
            List<Batch> batches = batchRepository.findByProductOrderByEntryDateAsc(product);
            
            for (Batch batch : batches) {
                List<Inventory> inventories = inventoryRepository.findByBatch(batch);
                for (Inventory inventory : inventories) {
                    if (inventory.getWarehouse().equals(warehouse) && 
                        inventory.getCurrentStock() != null && inventory.getCurrentStock() > 0) {
                        return batch.getPurchasePrice() != null ? batch.getPurchasePrice() : BigDecimal.ZERO;
                    }
                }
            }
        } catch (Exception e) {
            // Si hay error, usar precio base
        }
        return product.getBasePrice() != null ? product.getBasePrice() : BigDecimal.ZERO;
    }

    /**
     * LIFO (Last In, First Out) - precio del lote más reciente
     */
    private BigDecimal calculateLIFOPrice(Product product, Warehouse warehouse) {
        try {
            List<Batch> batches = batchRepository.findByProductOrderByEntryDateDesc(product);
            
            for (Batch batch : batches) {
                List<Inventory> inventories = inventoryRepository.findByBatch(batch);
                for (Inventory inventory : inventories) {
                    if (inventory.getWarehouse().equals(warehouse) && 
                        inventory.getCurrentStock() != null && inventory.getCurrentStock() > 0) {
                        return batch.getPurchasePrice() != null ? batch.getPurchasePrice() : BigDecimal.ZERO;
                    }
                }
            }
        } catch (Exception e) {
            // Si hay error, usar precio base
        }
        return product.getBasePrice() != null ? product.getBasePrice() : BigDecimal.ZERO;
    }

    /**
     * LILO (Last In, Last Out) - precio del lote que saldrá último
     */
    private BigDecimal calculateLILOPrice(Product product, Warehouse warehouse) {
        try {
            List<Batch> batches = batchRepository.findByProductOrderByEntryDateAsc(product);
            
            for (int i = batches.size() - 1; i >= 0; i--) {
                Batch batch = batches.get(i);
                List<Inventory> inventories = inventoryRepository.findByBatch(batch);
                for (Inventory inventory : inventories) {
                    if (inventory.getWarehouse().equals(warehouse) && 
                        inventory.getCurrentStock() != null && inventory.getCurrentStock() > 0) {
                        return batch.getPurchasePrice() != null ? batch.getPurchasePrice() : BigDecimal.ZERO;
                    }
                }
            }
        } catch (Exception e) {
            // Si hay error, usar precio base
        }
        return product.getBasePrice() != null ? product.getBasePrice() : BigDecimal.ZERO;
    }

    /**
     * Calcula productos próximos a vencer
     */
    public List<Batch> getBatchesNearExpiration(Warehouse warehouse, Integer daysThreshold) {
        Date thresholdDate = new Date(System.currentTimeMillis() + (daysThreshold * 24L * 60L * 60L * 1000L));
        return batchRepository.findExpiringBatches(thresholdDate, Batch.BatchStatus.ACTIVE);
    }
}
