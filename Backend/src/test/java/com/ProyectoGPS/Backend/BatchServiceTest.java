package com.ProyectoGPS.Backend;

import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.repository.BatchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ProyectoGPS.Backend.service.BatchService;

class BatchServiceTest {

    @Mock
    private BatchRepository batchRepository;

    @InjectMocks
    private BatchService batchService;

    private Batch batch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Product product = new Product();
        // NO seteamos el ID

        batch = new Batch();
        batch.setId(1L);
        batch.setBatchNumber("BN-001");
        batch.setProduct(product); // Solo instancia, sin ID
        batch.setManufacturingDate(new Date());
        batch.setExpirationDate(new Date());
        batch.setStatus(Batch.BatchStatus.ACTIVE);
        batch.setPurchasePrice(new BigDecimal("100.00"));
        batch.setCostPrice(new BigDecimal("80.00"));
        batch.setSupplier("Proveedor SA");
        batch.setInvoiceNumber("INV-123");
        batch.setStorageConditions("Seco");
        batch.setInitialQuantity(50);
        batch.setQuantity(30);
    }

    @Test
    void testGetAllBatches() {
        when(batchRepository.findAll()).thenReturn(List.of(batch));
        List<Batch> batches = batchService.getAllBatches();
        assertEquals(1, batches.size());
        assertEquals("BN-001", batches.get(0).getBatchNumber());
        verify(batchRepository).findAll();
    }

    @Test
    void testGetBatchById_found() {
        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        Optional<Batch> result = batchService.getBatchById(1L);
        assertTrue(result.isPresent());
        assertEquals("BN-001", result.get().getBatchNumber());
        verify(batchRepository).findById(1L);
    }

    @Test
    void testCreateBatch() {
        when(batchRepository.save(batch)).thenReturn(batch);
        Batch saved = batchService.createBatch(batch);
        assertNotNull(saved);
        assertEquals("BN-001", saved.getBatchNumber());
        verify(batchRepository).save(batch);
    }

    @Test
    void testUpdateBatch_found() {
        Batch details = new Batch();
        details.setBatchNumber("BN-002");
        details.setProduct(batch.getProduct());
        details.setManufacturingDate(new Date());
        details.setExpirationDate(new Date());
        details.setStatus(Batch.BatchStatus.EXPIRED);
        details.setPurchasePrice(new BigDecimal("120.00"));
        details.setCostPrice(new BigDecimal("90.00"));
        details.setSupplier("Otro Proveedor");
        details.setInvoiceNumber("INV-999");
        details.setStorageConditions("Refrigerado");
        details.setInitialQuantity(60);
        details.setQuantity(40);

        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        when(batchRepository.save(any(Batch.class))).thenAnswer(i -> i.getArgument(0));

        Batch updated = batchService.updateBatch(1L, details);
        assertEquals("BN-002", updated.getBatchNumber());
        assertEquals(Batch.BatchStatus.EXPIRED, updated.getStatus());
        assertEquals("INV-999", updated.getInvoiceNumber());
        verify(batchRepository).save(any(Batch.class));
    }

    @Test
    void testUpdateBatch_notFound() {
        when(batchRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> batchService.updateBatch(99L, batch));
        assertTrue(ex.getMessage().contains("Lote no encontrado"));
    }

    @Test
    void testDeleteBatch_found() {
        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        doNothing().when(batchRepository).delete(batch);

        batchService.deleteBatch(1L);
        verify(batchRepository).delete(batch);
    }

    @Test
    void testDeleteBatch_notFound() {
        when(batchRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> batchService.deleteBatch(99L));
        assertTrue(ex.getMessage().contains("Lote no encontrado"));
    }

    @Test
    void testGetBatchesByWarehouse_found() {
        when(batchRepository.findBatchesByWarehouse(2L)).thenReturn(List.of(batch));
        List<Batch> result = batchService.getBatchesByWarehouse(2L);
        assertEquals(1, result.size());
        verify(batchRepository).findBatchesByWarehouse(2L);
    }

    @Test
    void testGetBatchesByWarehouse_empty_returnsActive() {
        when(batchRepository.findBatchesByWarehouse(3L)).thenReturn(List.of());
        when(batchRepository.findByStatus(Batch.BatchStatus.ACTIVE)).thenReturn(List.of(batch));
        List<Batch> result = batchService.getBatchesByWarehouse(3L);
        assertEquals(1, result.size());
        assertEquals(Batch.BatchStatus.ACTIVE, result.get(0).getStatus());
        verify(batchRepository).findByStatus(Batch.BatchStatus.ACTIVE);
    }

    @Test
    void testGetActiveBatches() {
        when(batchRepository.findByStatus(Batch.BatchStatus.ACTIVE)).thenReturn(List.of(batch));
        List<Batch> result = batchService.getActiveBatches();
        assertEquals(1, result.size());
        verify(batchRepository).findByStatus(Batch.BatchStatus.ACTIVE);
    }
}