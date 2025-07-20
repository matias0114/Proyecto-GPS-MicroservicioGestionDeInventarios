package com.ProyectoGPS.Backend;

import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ProyectoGPS.Backend.service.WarehouseService;

class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Central");
        warehouse.setLocation("Av. Principal 123");
        warehouse.setCapacity(1000);
        warehouse.setStatus("ACTIVE");
    }

    @Test
    void testGetAllWarehouses() {
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));

        List<Warehouse> result = warehouseService.getAllWarehouses();

        assertEquals(1, result.size());
        assertEquals("Central", result.get(0).getName());
        verify(warehouseRepository).findAll();
    }

    @Test
    void testCreateWarehouse() {
        when(warehouseRepository.save(warehouse)).thenReturn(warehouse);

        Warehouse created = warehouseService.createWarehouse(warehouse);

        assertNotNull(created);
        assertEquals("Central", created.getName());
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void testGetWarehouseById_found() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        Optional<Warehouse> opt = warehouseService.getWarehouseById(1L);
        assertTrue(opt.isPresent());
        assertEquals("Central", opt.get().getName());
    }

    @Test
    void testGetWarehouseById_notFound() {
        when(warehouseRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Warehouse> opt = warehouseService.getWarehouseById(2L);
        assertFalse(opt.isPresent());
    }

    @Test
    void testUpdateWarehouse_ok() {
        Warehouse newDetails = new Warehouse();
        newDetails.setName("Secundaria");
        newDetails.setLocation("Calle 2");
        newDetails.setCapacity(500);
        newDetails.setStatus("INACTIVE");

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse updated = warehouseService.updateWarehouse(1L, newDetails);

        assertEquals("Secundaria", updated.getName());
        assertEquals("Calle 2", updated.getLocation());
        assertEquals(500, updated.getCapacity());
        assertEquals("INACTIVE", updated.getStatus());
        verify(warehouseRepository).save(any(Warehouse.class));
    }

    @Test
    void testUpdateWarehouse_notFound() {
        Warehouse newDetails = new Warehouse();
        when(warehouseRepository.findById(99L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> warehouseService.updateWarehouse(99L, newDetails));
        assertTrue(ex.getMessage().contains("Bodega no encontrada"));
    }

    @Test
    void testDeleteWarehouse_ok() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        doNothing().when(warehouseRepository).delete(warehouse);

        warehouseService.deleteWarehouse(1L);
        verify(warehouseRepository).delete(warehouse);
    }

    @Test
    void testDeleteWarehouse_notFound() {
        when(warehouseRepository.findById(77L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> warehouseService.deleteWarehouse(77L));
        assertTrue(ex.getMessage().contains("Bodega no encontrada"));
    }
}