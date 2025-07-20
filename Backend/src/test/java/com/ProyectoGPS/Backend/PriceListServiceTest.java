package com.ProyectoGPS.Backend;

import com.ProyectoGPS.Backend.dto.PriceListDTO;
import com.ProyectoGPS.Backend.model.PriceList;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.repository.PriceListRepository;
import com.ProyectoGPS.Backend.repository.ProductRepository;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ProyectoGPS.Backend.service.PriceListService;

class PriceListServiceTest {

    @Mock
    private PriceListRepository priceListRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private PriceListService priceListService;

    private PriceList priceList;
    private Product product;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product(); // sin id
        warehouse = new Warehouse(); // sin id

        priceList = new PriceList();
        priceList.setId(1L);
        priceList.setName("Lista 1");
        priceList.setValidFrom(new Date());
        priceList.setActive(true);
        priceList.setSalePrice(new BigDecimal("100.00"));
        priceList.setCostPrice(new BigDecimal("70.00"));
        priceList.setProduct(product);
        priceList.setWarehouse(warehouse);
    }

    @Test
    void testGetAllPriceLists() {
        when(priceListRepository.findAll()).thenReturn(List.of(priceList));
        List<PriceList> result = priceListService.getAllPriceLists();
        assertEquals(1, result.size());
        assertEquals("Lista 1", result.get(0).getName());
        verify(priceListRepository).findAll();
    }

    @Test
    void testGetPriceListById_found() {
        when(priceListRepository.findById(1L)).thenReturn(Optional.of(priceList));
        Optional<PriceList> result = priceListService.getPriceListById(1L);
        assertTrue(result.isPresent());
        assertEquals("Lista 1", result.get().getName());
        verify(priceListRepository).findById(1L);
    }

    @Test
    void testCreatePriceList() {
        PriceList input = new PriceList();
        input.setName("Nueva");
        input.setProductId(11L);
        input.setWarehouseId(12L);

        when(productRepository.findById(11L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(12L)).thenReturn(Optional.of(warehouse));
        when(priceListRepository.save(any(PriceList.class))).thenAnswer(i -> i.getArgument(0));

        PriceList saved = priceListService.createPriceList(input);

        assertNotNull(saved);
        assertEquals("Nueva", saved.getName());
        assertEquals(product, saved.getProduct());
        assertEquals(warehouse, saved.getWarehouse());
        verify(priceListRepository).save(any(PriceList.class));
    }

    @Test
    void testCreatePriceList_productNotFound() {
        PriceList input = new PriceList();
        input.setName("Erronea");
        input.setProductId(99L);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> priceListService.createPriceList(input));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void testUpdatePriceList_ok() {
        PriceList details = new PriceList();
        details.setName("Actualizada");
        details.setValidFrom(new Date());
        details.setActive(false);
        details.setProduct(product);
        details.setWarehouse(warehouse);
        details.setSalePrice(new BigDecimal("140.00"));

        when(priceListRepository.findById(1L)).thenReturn(Optional.of(priceList));
        when(priceListRepository.save(any(PriceList.class))).thenAnswer(i -> i.getArgument(0));

        PriceList updated = priceListService.updatePriceList(1L, details);
        assertEquals("Actualizada", updated.getName());
        assertEquals(new BigDecimal("140.00"), updated.getSalePrice());
        assertFalse(updated.getActive());
        verify(priceListRepository).save(any(PriceList.class));
    }

    @Test
    void testUpdatePriceList_notFound() {
        PriceList details = new PriceList();
        when(priceListRepository.findById(9L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> priceListService.updatePriceList(9L, details));
        assertTrue(ex.getMessage().contains("Lista de precios no encontrada"));
    }

    @Test
    void testDeletePriceList_ok() {
        when(priceListRepository.findById(1L)).thenReturn(Optional.of(priceList));
        doNothing().when(priceListRepository).delete(priceList);
        priceListService.deletePriceList(1L);
        verify(priceListRepository).delete(priceList);
    }

    @Test
    void testDeletePriceList_notFound() {
        when(priceListRepository.findById(77L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> priceListService.deletePriceList(77L));
        assertTrue(ex.getMessage().contains("Lista de precios no encontrada"));
    }

    @Test
    void testGetCurrentPrices() {
        when(priceListRepository.findCurrentActivePrices()).thenReturn(List.of(priceList));
        List<PriceList> list = priceListService.getCurrentPrices();
        assertEquals(1, list.size());
        verify(priceListRepository).findCurrentActivePrices();
    }

    @Test
    void testGetPricesByProductId() {
        when(priceListRepository.findByProduct_Id(5L)).thenReturn(List.of(priceList));
        List<PriceList> list = priceListService.getPricesByProductId(5L);
        assertEquals(1, list.size());
        verify(priceListRepository).findByProduct_Id(5L);
    }

    @Test
    void testGetPricesByDate() {
        Date today = new Date();
        when(priceListRepository.findActiveByDate(today)).thenReturn(List.of(priceList));
        List<PriceList> list = priceListService.getPricesByDate(today);
        assertEquals(1, list.size());
        verify(priceListRepository).findActiveByDate(today);
    }

    @Test
    void testCreatePriceListFromDTO() {
        PriceListDTO dto = new PriceListDTO();
        dto.setName("Desde DTO");
        dto.setProductId(1L);
        dto.setWarehouseId(2L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(2L)).thenReturn(Optional.of(warehouse));
        when(priceListRepository.save(any(PriceList.class))).thenAnswer(i -> i.getArgument(0));

        PriceList saved = priceListService.createPriceListFromDTO(dto);
        assertEquals("Desde DTO", saved.getName());
        assertEquals(product, saved.getProduct());
        assertEquals(warehouse, saved.getWarehouse());
    }

    @Test
    void testUpdatePriceListFromDTO_ok() {
        PriceListDTO dto = new PriceListDTO();
        dto.setName("DTO Actualizada");
        dto.setProductId(1L);

        when(priceListRepository.findById(1L)).thenReturn(Optional.of(priceList));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(priceListRepository.save(any(PriceList.class))).thenAnswer(i -> i.getArgument(0));

        PriceList updated = priceListService.updatePriceListFromDTO(1L, dto);
        assertEquals("DTO Actualizada", updated.getName());
        assertEquals(product, updated.getProduct());
    }
}