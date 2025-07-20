package com.ProyectoGPS.Backend.Integracion;

import com.ProyectoGPS.Backend.model.PriceList;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.repository.PriceListRepository;
import com.ProyectoGPS.Backend.repository.ProductRepository;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;
import com.ProyectoGPS.Backend.service.PriceListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PriceListIntegrationTest {

    @Autowired private PriceListService priceListService;
    @Autowired private PriceListRepository priceListRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private WarehouseRepository warehouseRepository;

    private Product product;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        priceListRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();

        product = new Product();
        product.setCode("C100"); product.setName("TestProd"); product = productRepository.save(product);

        warehouse = new Warehouse();
        warehouse.setName("Bodega1"); warehouse.setLocation("Ubicacion1"); warehouse.setCapacity(30); warehouse.setStatus("ACTIVE");
        warehouse = warehouseRepository.save(warehouse);
    }

    @Test
    void testCreateAndUpdatePriceList() {
        PriceList pl = new PriceList();
        pl.setName("Precio Test");
        pl.setProduct(product);
        pl.setWarehouse(warehouse);
        pl.setSalePrice(new BigDecimal("1500"));
        pl.setValidFrom(new Date());
        pl.setActive(true);

        PriceList saved = priceListService.createPriceList(pl);
        assertThat(saved.getId()).isNotNull();

        PriceList cambios = new PriceList();
        cambios.setName("Nuevo Nombre");
        cambios.setProduct(product);
        cambios.setWarehouse(warehouse);
        cambios.setSalePrice(new BigDecimal("1900"));
        cambios.setValidFrom(new Date());
        cambios.setActive(false);

        PriceList updated = priceListService.updatePriceList(saved.getId(), cambios);
        assertThat(updated.getName()).isEqualTo("Nuevo Nombre");
        assertThat(updated.getSalePrice()).isEqualTo(new BigDecimal("1900"));
        assertThat(updated.getActive()).isFalse();
    }

    @Test
    void testDeletePriceList() {
        PriceList pl = new PriceList();
        pl.setName("Borrar");
        pl.setProduct(product);
        pl.setWarehouse(warehouse);
        pl.setSalePrice(new BigDecimal("1200"));
        pl.setValidFrom(new Date());
        pl.setActive(true);

        PriceList saved = priceListService.createPriceList(pl);

        priceListService.deletePriceList(saved.getId());

        assertThat(priceListRepository.findById(saved.getId())).isEmpty();
    }
}