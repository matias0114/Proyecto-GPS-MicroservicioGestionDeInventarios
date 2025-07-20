package com.ProyectoGPS.Backend;

import com.ProyectoGPS.Backend.dto.UpdateInventoryDTO;
import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.repository.BatchRepository;
import com.ProyectoGPS.Backend.repository.InventoryRepository;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ProyectoGPS.Backend.service.InventoryService;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private BatchRepository batchRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Warehouse warehouse;
    private Batch batch;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        warehouse = new Warehouse();
        batch = new Batch();

        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setWarehouse(warehouse);
        inventory.setBatch(batch);
        inventory.setQuantity(10);
        inventory.setCurrentStock(10);
        inventory.setInventoryType("GENERAL");
        inventory.setLastUpdate(new Date());
    }

    @Test
    void testGetAllInventories() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));
        List<Inventory> list = inventoryService.getAllInventories();
        assertEquals(1, list.size());
        verify(inventoryRepository).findAll();
    }

    @Test
    void testCreateSweepInventory() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(batchRepository.findById(2L)).thenReturn(Optional.of(batch));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        Inventory inv = inventoryService.createSweepInventory(1L, 2L, 15);

        assertNotNull(inv);
        assertEquals("BARRIDO", inv.getInventoryType());
        assertEquals(15, inv.getQuantity());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void testCreateSelectiveInventory() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(batchRepository.findById(2L)).thenReturn(Optional.of(batch));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        Inventory inv = inventoryService.createSelectiveInventory(1L, 2L, 20);

        assertNotNull(inv);
        assertEquals("SELECTIVO", inv.getInventoryType());
        assertEquals(20, inv.getQuantity());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void testCreateGeneralInventory() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        List<Inventory> existing = new ArrayList<>();
        Inventory inv = new Inventory();
        inv.setId(2L);
        inv.setWarehouse(warehouse);
        inv.setQuantity(7);
        existing.add(inv);

        when(inventoryRepository.findByWarehouse_Id(1L)).thenReturn(existing);
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        List<Inventory> updated = inventoryService.createGeneralInventory(1L);
        assertEquals(1, updated.size());
        assertEquals("GENERAL", updated.get(0).getInventoryType());
        verify(inventoryRepository).findByWarehouse_Id(1L);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void testGetStockByWarehouse() {
        when(inventoryRepository.findByWarehouse_Id(3L)).thenReturn(List.of(inventory));
        List<Inventory> list = inventoryService.getStockByWarehouse(3L);
        assertEquals(1, list.size());
        verify(inventoryRepository).findByWarehouse_Id(3L);
    }

    @Test
    void testGetStockByBatch() {
        when(inventoryRepository.findByBatchId(4L)).thenReturn(List.of(inventory));
        List<Inventory> list = inventoryService.getStockByBatch(4L);
        assertEquals(1, list.size());
        verify(inventoryRepository).findByBatchId(4L);
    }

    @Test
    void testUpdateInventory_ok() {
        UpdateInventoryDTO dto = new UpdateInventoryDTO();
        dto.setQuantity(30);
        dto.setCurrentStock(28);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        Inventory updated = inventoryService.updateInventory(1L, dto);

        assertEquals(30, updated.getQuantity());
        assertEquals(28, updated.getCurrentStock());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void testUpdateInventory_notFound() {
        UpdateInventoryDTO dto = new UpdateInventoryDTO();
        when(inventoryRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            inventoryService.updateInventory(99L, dto));
        assertTrue(ex.getMessage().contains("Inventario no encontrado"));
    }

    @Test
    void testDeleteInventory_ok() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        doNothing().when(inventoryRepository).delete(inventory);

        inventoryService.deleteInventory(1L);

        verify(inventoryRepository).delete(inventory);
    }

    @Test
    void testDeleteInventory_notFound() {
        when(inventoryRepository.findById(77L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            inventoryService.deleteInventory(77L));
        assertTrue(ex.getMessage().contains("Inventario no encontrado"));
    }
}