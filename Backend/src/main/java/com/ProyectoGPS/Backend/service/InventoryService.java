package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.UpdateInventoryDTO;
import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.repository.InventoryRepository;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;
import com.ProyectoGPS.Backend.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private BatchRepository batchRepository;

    // Obtener todos los inventarios
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    // Método para realizar barrido de inventario
    public Inventory createSweepInventory(Long warehouseId, Long batchId, Integer quantity) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        
        Batch batch = batchRepository.findById(batchId)
            .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        Inventory inventory = new Inventory();
        inventory.setWarehouse(warehouse);
        inventory.setBatch(batch);
        inventory.setQuantity(quantity);
        inventory.setCurrentStock(quantity); // Inicializar stock actual
        inventory.setInventoryType("BARRIDO");
        inventory.setLastUpdate(new Date());

        return inventoryRepository.save(inventory);
    }

    // Método para realizar inventario selectivo
    public Inventory createSelectiveInventory(Long warehouseId, Long batchId, Integer quantity) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        
        Batch batch = batchRepository.findById(batchId)
            .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        Inventory inventory = new Inventory();
        inventory.setWarehouse(warehouse);
        inventory.setBatch(batch);
        inventory.setQuantity(quantity);
        inventory.setCurrentStock(quantity); // Inicializar stock actual
        inventory.setInventoryType("SELECTIVO");
        inventory.setLastUpdate(new Date());

        return inventoryRepository.save(inventory);
    }

    // Método para realizar inventario general
    public List<Inventory> createGeneralInventory(Long warehouseId) {
        // Primero verificamos que la bodega existe
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

        // Obtenemos todos los inventarios existentes de esta bodega
        List<Inventory> existingInventories = inventoryRepository.findByWarehouse_Id(warehouseId);
        
        // Actualizamos cada inventario como parte del inventario general
        for (Inventory inventory : existingInventories) {
            inventory.setInventoryType("GENERAL");
            inventory.setLastUpdate(new Date());
            // Mantener el currentStock existente
            if (inventory.getCurrentStock() == null) {
                inventory.setCurrentStock(inventory.getQuantity());
            }
            inventoryRepository.save(inventory);
        }

        return existingInventories;
    }

    // Método para obtener el stock por bodega
    public List<Inventory> getStockByWarehouse(Long warehouseId) {
        return inventoryRepository.findByWarehouse_Id(warehouseId);
    }

    // Método para obtener el stock por lote
    public List<Inventory> getStockByBatch(Long batchId) {
        return inventoryRepository.findByBatchId(batchId);
    }

    // Método para actualizar inventario
    public Inventory updateInventory(Long inventoryId, UpdateInventoryDTO updateInventoryDTO) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + inventoryId));
        
        // Actualizar solo los campos que pueden cambiar
        if (updateInventoryDTO.getQuantity() != null) {
            inventory.setQuantity(updateInventoryDTO.getQuantity());
        }
        if (updateInventoryDTO.getCurrentStock() != null) {
            inventory.setCurrentStock(updateInventoryDTO.getCurrentStock());
        }
        inventory.setLastUpdate(new Date());
        
        return inventoryRepository.save(inventory);
    }

    // Método para eliminar inventario
    @Transactional
    public void deleteInventory(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + inventoryId));
        inventoryRepository.delete(inventory);
    }

    // Método para obtener inventario por lote y bodega específicos
    public Inventory getInventoryByBatchAndWarehouse(Long batchId, Long warehouseId) {
        // Obtenemos la lista de inventarios y tomamos el primero con stock disponible
        List<Inventory> inventories = inventoryRepository.findByBatchIdAndWarehouseId(batchId, warehouseId);
        
        // Buscamos el primer inventario con stock disponible
        return inventories.stream()
                .filter(inv -> inv.getCurrentStock() != null && inv.getCurrentStock() > 0)
                .findFirst()
                .orElse(inventories.isEmpty() ? null : inventories.get(0));
    }

    // Método para obtener inventario por ID
    public Inventory getInventoryById(Long inventoryId) {
        return inventoryRepository.findById(inventoryId).orElse(null);
    }

    // Método para reducir stock
    @Transactional
    public boolean reduceStock(Long inventoryId, Integer quantityToReduce, String reason) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + inventoryId));
            
            if (inventory.getCurrentStock() < quantityToReduce) {
                return false; // Stock insuficiente
            }
            
            inventory.setCurrentStock(inventory.getCurrentStock() - quantityToReduce);
            inventory.setLastUpdate(new Date());
            inventoryRepository.save(inventory);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Método para incrementar stock (para cancelaciones)
    @Transactional
    public boolean addStock(Long inventoryId, Integer quantityToAdd, String reason) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + inventoryId));
            
            inventory.setCurrentStock(inventory.getCurrentStock() + quantityToAdd);
            inventory.setLastUpdate(new Date());
            inventoryRepository.save(inventory);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
