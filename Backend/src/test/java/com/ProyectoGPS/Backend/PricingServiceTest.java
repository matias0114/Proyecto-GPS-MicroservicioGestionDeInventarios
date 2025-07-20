package com.ProyectoGPS.Backend;

import com.ProyectoGPS.Backend.model.*;
import com.ProyectoGPS.Backend.repository.BatchRepository;
import com.ProyectoGPS.Backend.repository.InventoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ProyectoGPS.Backend.service.PricingService;

class PricingServiceTest {

    @Mock
    private BatchRepository batchRepository;
    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private PricingService pricingService;

    private Product product;
    private Warehouse warehouse;
    private Batch batch;
    private Inventory inventory;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        product = new Product();

        // set basePrice
        Field basePriceField = Product.class.getDeclaredField("basePrice");
        basePriceField.setAccessible(true);
        basePriceField.set(product, new BigDecimal("500.00"));

        warehouse = new Warehouse();

        batch = new Batch();
        batch.setPurchasePrice(new BigDecimal("200.00"));

        inventory = new Inventory();
        inventory.setWarehouse(warehouse);
        inventory.setBatch(batch);
        inventory.setCurrentStock(10);
    }

    @Test
    void testCalculatePrice_DefaultBase() throws Exception {
        // set basePrice a otro valor
        Field basePriceField = Product.class.getDeclaredField("basePrice");
        basePriceField.setAccessible(true);
        basePriceField.set(product, new BigDecimal("600.00"));

        // defaultPricingMethod = null
        Field pricingMethodField = Product.class.getDeclaredField("defaultPricingMethod");
        pricingMethodField.setAccessible(true);
        pricingMethodField.set(product, null);

        BigDecimal price = pricingService.calculatePrice(product, warehouse);
        assertEquals(new BigDecimal("600.00"), price);
    }

    @Test
    void testCalculatePrice_LastPurchase() throws Exception {
        // set defaultPricingMethod = LAST_PURCHASE
        Field pricingMethodField = Product.class.getDeclaredField("defaultPricingMethod");
        pricingMethodField.setAccessible(true);
        pricingMethodField.set(product, Product.PricingMethod.LAST_PURCHASE);

        when(batchRepository.findByProductOrderByEntryDateDesc(product)).thenReturn(List.of(batch));
        BigDecimal price = pricingService.calculatePrice(product, warehouse);
        assertEquals(new BigDecimal("200.00"), price);
    }

    @Test
    void testCalculatePrice_FIFO() throws Exception {
        Field pricingMethodField = Product.class.getDeclaredField("defaultPricingMethod");
        pricingMethodField.setAccessible(true);
        pricingMethodField.set(product, Product.PricingMethod.FIFO);

        when(batchRepository.findByProductOrderByEntryDateAsc(product)).thenReturn(List.of(batch));
        when(inventoryRepository.findByBatch(batch)).thenReturn(List.of(inventory));
        BigDecimal price = pricingService.calculatePrice(product, warehouse);
        assertEquals(new BigDecimal("200.00"), price);
    }

    @Test
    void testCalculatePrice_LIFO() throws Exception {
        Field pricingMethodField = Product.class.getDeclaredField("defaultPricingMethod");
        pricingMethodField.setAccessible(true);
        pricingMethodField.set(product, Product.PricingMethod.LIFO);

        when(batchRepository.findByProductOrderByEntryDateDesc(product)).thenReturn(List.of(batch));
        when(inventoryRepository.findByBatch(batch)).thenReturn(List.of(inventory));
        BigDecimal price = pricingService.calculatePrice(product, warehouse);
        assertEquals(new BigDecimal("200.00"), price);
    }

    @Test
    void testCalculatePrice_LILO() throws Exception {
        Field pricingMethodField = Product.class.getDeclaredField("defaultPricingMethod");
        pricingMethodField.setAccessible(true);
        pricingMethodField.set(product, Product.PricingMethod.LILO);

        when(batchRepository.findByProductOrderByEntryDateAsc(product)).thenReturn(List.of(batch));
        when(inventoryRepository.findByBatch(batch)).thenReturn(List.of(inventory));
        BigDecimal price = pricingService.calculatePrice(product, warehouse);
        assertEquals(new BigDecimal("200.00"), price);
    }

    @Test
    void testCalculatePrice_FallbackToBasePriceWhenNoBatches() throws Exception {
        Field pricingMethodField = Product.class.getDeclaredField("defaultPricingMethod");
        pricingMethodField.setAccessible(true);
        pricingMethodField.set(product, Product.PricingMethod.LAST_PURCHASE);

        when(batchRepository.findByProductOrderByEntryDateDesc(product)).thenReturn(Collections.emptyList());

        Field basePriceField = Product.class.getDeclaredField("basePrice");
        basePriceField.setAccessible(true);
        basePriceField.set(product, new BigDecimal("500.00"));

        BigDecimal price = pricingService.calculatePrice(product, warehouse);
        assertEquals(new BigDecimal("500.00"), price);
    }

    @Test
    void testCalculatePrice_ByIdAndQty() {
        BigDecimal price = pricingService.calculatePrice(10L, 5);
        assertEquals(BigDecimal.valueOf(1000), price);
    }

    @Test
    void testGetBatchesNearExpiration() {
        List<Batch> batches = List.of(batch);
        when(batchRepository.findExpiringBatches(any(Date.class), eq(Batch.BatchStatus.ACTIVE)))
                .thenReturn(batches);
        List<Batch> result = pricingService.getBatchesNearExpiration(warehouse, 10);
        assertEquals(1, result.size());
        verify(batchRepository).findExpiringBatches(any(Date.class), eq(Batch.BatchStatus.ACTIVE));
    }
}