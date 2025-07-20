package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByWarehouse_Id(Long warehouseId);
    List<Inventory> findByBatchId(Long batchId);
    List<Inventory> findByInventoryType(String inventoryType);
    List<Inventory> findByBatch(Batch batch);
    List<Inventory> findByWarehouse(Warehouse warehouse);
    List<Inventory> findByCurrentStockLessThan(Integer minimumStock);
    
    // Método para encontrar inventario específico por lote y bodega
    List<Inventory> findByBatchIdAndWarehouseId(Long batchId, Long warehouseId);
}
