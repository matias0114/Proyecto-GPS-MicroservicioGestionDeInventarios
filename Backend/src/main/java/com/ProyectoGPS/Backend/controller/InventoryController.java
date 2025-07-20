package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.InventoryDTO;
import com.ProyectoGPS.Backend.dto.UpdateInventoryDTO;
import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;

    // Endpoint para obtener todos los inventarios
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventories() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        List<InventoryDTO> inventoryDTOs = inventories.stream()
            .map(InventoryDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(inventoryDTOs);
    }

    // Endpoint para crear inventario por barrido
    @PostMapping("/sweep")
    public ResponseEntity<InventoryDTO> createSweepInventory(@RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryService.createSweepInventory(
            inventoryDTO.getWarehouseId(),
            inventoryDTO.getBatchId(),
            inventoryDTO.getQuantity()
        );
        InventoryDTO inventoryResponseDTO = new InventoryDTO(inventory);
        return ResponseEntity.ok(inventoryResponseDTO);
    }

    // Endpoint para crear inventario selectivo
    @PostMapping("/selective")
    public ResponseEntity<InventoryDTO> createSelectiveInventory(@RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryService.createSelectiveInventory(
            inventoryDTO.getWarehouseId(),
            inventoryDTO.getBatchId(),
            inventoryDTO.getQuantity()
        );
        InventoryDTO inventoryResponseDTO = new InventoryDTO(inventory);
        return ResponseEntity.ok(inventoryResponseDTO);
    }

    // Endpoint para crear inventario general
    @PostMapping("/general/{warehouseId}")
    public ResponseEntity<List<InventoryDTO>> createGeneralInventory(@PathVariable Long warehouseId) {
        List<Inventory> inventories = inventoryService.createGeneralInventory(warehouseId);
        List<InventoryDTO> inventoryDTOs = inventories.stream()
            .map(InventoryDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(inventoryDTOs);
    }

    // Endpoint para obtener stock por bodega
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryDTO>> getStockByWarehouse(@PathVariable Long warehouseId) {
        List<Inventory> inventories = inventoryService.getStockByWarehouse(warehouseId);
        List<InventoryDTO> inventoryDTOs = inventories.stream()
            .map(InventoryDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(inventoryDTOs);
    }

    // Endpoint para obtener stock por lote
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<InventoryDTO>> getStockByBatch(@PathVariable Long batchId) {
        List<Inventory> inventories = inventoryService.getStockByBatch(batchId);
        List<InventoryDTO> inventoryDTOs = inventories.stream()
            .map(InventoryDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(inventoryDTOs);
    }

    // Endpoint para actualizar inventario
    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryDTO> updateInventory(
        @PathVariable Long inventoryId,
        @RequestBody UpdateInventoryDTO updateInventoryDTO) {
        Inventory updatedInventory = inventoryService.updateInventory(inventoryId, updateInventoryDTO);
        InventoryDTO inventoryDTO = new InventoryDTO(updatedInventory);
        return ResponseEntity.ok(inventoryDTO);
    }

    // Endpoint para eliminar inventario
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.ok().build();
    }

    // Endpoint para obtener inventario específico por lote y bodega
    @GetMapping("/batch/{batchId}/warehouse/{warehouseId}")
    public ResponseEntity<InventoryDTO> getInventoryByBatchAndWarehouse(
            @PathVariable Long batchId, 
            @PathVariable Long warehouseId) {
        Inventory inventory = inventoryService.getInventoryByBatchAndWarehouse(batchId, warehouseId);
        if (inventory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new InventoryDTO(inventory));
    }

    // Endpoint para obtener inventario específico por ID
    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long inventoryId) {
        Inventory inventory = inventoryService.getInventoryById(inventoryId);
        if (inventory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new InventoryDTO(inventory));
    }

    // Endpoint para reducir stock por venta
    @PutMapping("/{inventoryId}/reduce-stock")
    public ResponseEntity<Void> reduceStock(
            @PathVariable Long inventoryId, 
            @RequestBody java.util.Map<String, Object> request) {
        Integer quantity = (Integer) request.get("quantity");
        String reason = (String) request.getOrDefault("reason", "SALE");
        
        boolean success = inventoryService.reduceStock(inventoryId, quantity, reason);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para incrementar stock (por cancelaciones)
    @PutMapping("/{inventoryId}/add-stock")
    public ResponseEntity<Void> addStock(
            @PathVariable Long inventoryId, 
            @RequestBody java.util.Map<String, Object> request) {
        Integer quantity = (Integer) request.get("quantity");
        String reason = (String) request.getOrDefault("reason", "CANCELLATION");
        
        boolean success = inventoryService.addStock(inventoryId, quantity, reason);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
