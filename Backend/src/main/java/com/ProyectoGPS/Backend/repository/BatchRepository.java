package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Batch;
import com.ProyectoGPS.Backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    Batch findByBatchNumber(String batchNumber);
    
    List<Batch> findByProductAndStatusOrderByExpirationDateAsc(Product product, Batch.BatchStatus status);
    
    List<Batch> findByExpirationDateBetweenAndStatus(Date startDate, Date endDate, Batch.BatchStatus status);
    
    @Query("SELECT b FROM Batch b WHERE b.expirationDate <= :futureDate AND b.status = :status ORDER BY b.expirationDate ASC")
    List<Batch> findExpiringBatches(@Param("futureDate") Date futureDate, @Param("status") Batch.BatchStatus status);
    
    List<Batch> findByProductOrderByEntryDateDesc(Product product);
    
    List<Batch> findByProductOrderByEntryDateAsc(Product product);
    
    // Obtener lotes por bodega (a través de inventarios)
    @Query("SELECT DISTINCT b FROM Batch b JOIN Inventory i ON b.id = i.batch.id WHERE i.warehouse.id = :warehouseId")
    List<Batch> findBatchesByWarehouse(@Param("warehouseId") Long warehouseId);
    
    // Obtener todos los lotes activos (método alternativo)
    List<Batch> findByStatusOrderByExpirationDateAsc(Batch.BatchStatus status);
    
    // Obtener lotes por estado
    List<Batch> findByStatus(Batch.BatchStatus status);
}
