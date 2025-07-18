package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.InventoryDTO;
import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;

    // Endpoint para obtener todos los inventarios
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventories);
    }

    // Endpoint para crear inventario por barrido
    @PostMapping("/sweep")
    public ResponseEntity<Inventory> createSweepInventory(@RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryService.createSweepInventory(
            inventoryDTO.getWarehouseId(),
            inventoryDTO.getBatchId(),
            inventoryDTO.getQuantity()
        );
        return ResponseEntity.ok(inventory);
    }

    // Endpoint para crear inventario selectivo
    @PostMapping("/selective")
    public ResponseEntity<Inventory> createSelectiveInventory(@RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryService.createSelectiveInventory(
            inventoryDTO.getWarehouseId(),
            inventoryDTO.getBatchId(),
            inventoryDTO.getQuantity()
        );
        return ResponseEntity.ok(inventory);
    }

    // Endpoint para crear inventario general
    @PostMapping("/general/{warehouseId}")
    public ResponseEntity<List<Inventory>> createGeneralInventory(@PathVariable Long warehouseId) {
        List<Inventory> inventories = inventoryService.createGeneralInventory(warehouseId);
        return ResponseEntity.ok(inventories);
    }

    // Endpoint para obtener stock por bodega
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Inventory>> getStockByWarehouse(@PathVariable Long warehouseId) {
        List<Inventory> inventories = inventoryService.getStockByWarehouse(warehouseId);
        return ResponseEntity.ok(inventories);
    }

    // Endpoint para obtener stock por lote
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<Inventory>> getStockByBatch(@PathVariable Long batchId) {
        List<Inventory> inventories = inventoryService.getStockByBatch(batchId);
        return ResponseEntity.ok(inventories);
    }

    // Endpoint para actualizar inventario
    @PutMapping("/{inventoryId}")
    public ResponseEntity<Inventory> updateInventory(
        @PathVariable Long inventoryId,
        @RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryService.updateInventory(inventoryId, inventoryDTO.getQuantity());
        return ResponseEntity.ok(inventory);
    }
}
