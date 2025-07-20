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
}
