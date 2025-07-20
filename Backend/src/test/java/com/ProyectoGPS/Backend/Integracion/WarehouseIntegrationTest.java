package com.ProyectoGPS.Backend.Integracion;

import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;
import com.ProyectoGPS.Backend.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class WarehouseIntegrationTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        warehouseRepository.deleteAll();
    }

    @Test
    void testCreateAndGetWarehouse() {
        Warehouse w = new Warehouse();
        w.setName("Bodega Central");
        w.setLocation("Calle Uno 123");
        w.setCapacity(100);
        w.setStatus("ACTIVE");

        Warehouse saved = warehouseService.createWarehouse(w);

        Optional<Warehouse> found = warehouseService.getWarehouseById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bodega Central");
        assertThat(found.get().getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testGetAllWarehouses() {
        Warehouse w1 = new Warehouse();
        w1.setName("B1");
        w1.setLocation("Loc1");
        w1.setCapacity(10);
        w1.setStatus("ACTIVE");

        Warehouse w2 = new Warehouse();
        w2.setName("B2");
        w2.setLocation("Loc2");
        w2.setCapacity(20);
        w2.setStatus("INACTIVE");

        warehouseRepository.save(w1);
        warehouseRepository.save(w2);

        List<Warehouse> all = warehouseService.getAllWarehouses();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(Warehouse::getName).contains("B1", "B2");
    }

    @Test
    void testUpdateWarehouse() {
        Warehouse w = new Warehouse();
        w.setName("B1");
        w.setLocation("Loc1");
        w.setCapacity(10);
        w.setStatus("ACTIVE");
        Warehouse saved = warehouseRepository.save(w);

        Warehouse updated = new Warehouse();
        updated.setName("B1-Updated");
        updated.setLocation("Loc2");
        updated.setCapacity(22);
        updated.setStatus("INACTIVE");

        Warehouse result = warehouseService.updateWarehouse(saved.getId(), updated);

        assertThat(result.getName()).isEqualTo("B1-Updated");
        assertThat(result.getLocation()).isEqualTo("Loc2");
        assertThat(result.getCapacity()).isEqualTo(22);
        assertThat(result.getStatus()).isEqualTo("INACTIVE");
    }

    @Test
    void testDeleteWarehouse() {
        Warehouse w = new Warehouse();
        w.setName("BorrarMe");
        w.setLocation("LocX");
        w.setCapacity(1);
        w.setStatus("ACTIVE");
        Warehouse saved = warehouseRepository.save(w);

        warehouseService.deleteWarehouse(saved.getId());

        Optional<Warehouse> found = warehouseRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void testUpdateWarehouse_notFound() {
        Warehouse updated = new Warehouse();
        updated.setName("Nada");
        updated.setLocation("Nada");
        updated.setCapacity(0);
        updated.setStatus("INACTIVE");

        assertThatThrownBy(() -> warehouseService.updateWarehouse(9999L, updated))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Bodega no encontrada");
    }

    @Test
    void testDeleteWarehouse_notFound() {
        assertThatThrownBy(() -> warehouseService.deleteWarehouse(8888L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Bodega no encontrada");
    }
}