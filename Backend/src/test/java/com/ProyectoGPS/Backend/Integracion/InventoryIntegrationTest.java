package com.ProyectoGPS.Backend.Integracion;

import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.model.Inventory;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.repository.BatchRepository;
import com.ProyectoGPS.Backend.repository.InventoryRepository;
import com.ProyectoGPS.Backend.repository.ProductRepository;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;
import com.ProyectoGPS.Backend.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InventoryIntegrationTest {

    @Autowired private InventoryService inventoryService;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private BatchRepository batchRepository;
    @Autowired private ProductRepository productRepository;

    private Warehouse warehouse;
    private Batch batch;

    @BeforeEach
    void setUp() {
        inventoryRepository.deleteAll();
        batchRepository.deleteAll();
        warehouseRepository.deleteAll();
        productRepository.deleteAll();

        // 1. Guardar primero el producto
        Product product = new Product();
        product.setCode("P-001");
        product.setName("Producto de Inventario");
        product.setActive(true); // asegúrate que tu modelo tiene este campo si es obligatorio
        product = productRepository.save(product);

        // 2. Guardar la bodega
        warehouse = new Warehouse();
        warehouse.setName("BodegaTest");
        warehouse.setLocation("Loc1");
        warehouse.setCapacity(100);
        warehouse.setStatus("ACTIVE");
        warehouse = warehouseRepository.save(warehouse);

        // 3. Crear y guardar el lote con el producto persistido
        batch = new Batch();
        batch.setProduct(product);
        batch.setBatchNumber("LoteInv");
        batch.setExpirationDate(new Date());
        batch.setStatus(Batch.BatchStatus.ACTIVE);
        batch.setInitialQuantity(20);
        batch.setQuantity(20);
        batch = batchRepository.save(batch);
    }

    @Test
    void testCreateSweepInventory() {
        Inventory inv = inventoryService.createSweepInventory(warehouse.getId(), batch.getId(), 5);

        assertThat(inv.getId()).isNotNull();
        assertThat(inv.getWarehouse().getId()).isEqualTo(warehouse.getId());
        assertThat(inv.getBatch().getId()).isEqualTo(batch.getId());
        assertThat(inv.getInventoryType()).isEqualTo("BARRIDO");
        assertThat(inv.getQuantity()).isEqualTo(5);
    }

    @Test
    void testCreateSelectiveInventory() {
        Inventory inv = inventoryService.createSelectiveInventory(warehouse.getId(), batch.getId(), 3);
        assertThat(inv.getInventoryType()).isEqualTo("SELECTIVO");
    }

    @Test
    void testCreateGeneralInventory() {
        Inventory inv = inventoryService.createSweepInventory(warehouse.getId(), batch.getId(), 10);
        List<Inventory> all = inventoryService.createGeneralInventory(warehouse.getId());
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getInventoryType()).isEqualTo("GENERAL");
    }

    @Test
    void testUpdateAndDeleteInventory() {
        Inventory inv = inventoryService.createSweepInventory(warehouse.getId(), batch.getId(), 7);
        inv.setQuantity(15);
        inventoryRepository.save(inv);

        // Actualiza
        com.ProyectoGPS.Backend.dto.UpdateInventoryDTO update = new com.ProyectoGPS.Backend.dto.UpdateInventoryDTO();
        update.setQuantity(20);
        update.setCurrentStock(18);

        Inventory updated = inventoryService.updateInventory(inv.getId(), update);
        assertThat(updated.getQuantity()).isEqualTo(20);
        assertThat(updated.getCurrentStock()).isEqualTo(18);

        // Borra
        inventoryService.deleteInventory(inv.getId());
        assertThat(inventoryRepository.findById(inv.getId())).isEmpty();
    }
}