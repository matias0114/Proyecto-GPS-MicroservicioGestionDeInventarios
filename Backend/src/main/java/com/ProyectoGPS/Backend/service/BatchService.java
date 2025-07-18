package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Optional<Batch> getBatchById(Long id) {
        return batchRepository.findById(id);
    }

    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public Batch updateBatch(Long id, Batch batchDetails) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado con id: " + id));

        batch.setBatchNumber(batchDetails.getBatchNumber());
        batch.setProduct(batchDetails.getProduct());
        batch.setManufacturingDate(batchDetails.getManufacturingDate());
        batch.setExpirationDate(batchDetails.getExpirationDate());
        batch.setStatus(batchDetails.getStatus());
        batch.setPurchasePrice(batchDetails.getPurchasePrice());
        batch.setCostPrice(batchDetails.getCostPrice());
        batch.setSupplier(batchDetails.getSupplier());
        batch.setInvoiceNumber(batchDetails.getInvoiceNumber());
        batch.setStorageConditions(batchDetails.getStorageConditions());
        batch.setInitialQuantity(batchDetails.getInitialQuantity());
        batch.setQuantity(batchDetails.getQuantity());

        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado con id: " + id));
        batchRepository.delete(batch);
    }

    public List<Batch> getBatchesByWarehouse(Long warehouseId) {
        // Primero intentamos obtener lotes asociados a inventarios de esa bodega
        List<Batch> batchesWithInventory = batchRepository.findBatchesByWarehouse(warehouseId);
        
        // Si no hay lotes con inventario, devolvemos todos los lotes activos
        if (batchesWithInventory.isEmpty()) {
            return batchRepository.findByStatus(Batch.BatchStatus.ACTIVE);
        }
        
        return batchesWithInventory;
    }
    
    // Método adicional para obtener lotes activos
    public List<Batch> getActiveBatches() {
        return batchRepository.findByStatus(Batch.BatchStatus.ACTIVE);
    }
}
