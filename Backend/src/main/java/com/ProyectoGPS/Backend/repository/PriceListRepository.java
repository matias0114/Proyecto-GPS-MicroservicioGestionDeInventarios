package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Long> {
    List<PriceList> findByProductId(Long productId);
    
    @Query("SELECT p FROM PriceList p WHERE p.active = true AND p.validFrom <= :date AND (p.validTo IS NULL OR p.validTo >= :date)")
    List<PriceList> findActiveByDate(@Param("date") Date date);
    
    @Query("SELECT p FROM PriceList p WHERE p.active = true AND p.validFrom <= CURRENT_DATE AND (p.validTo IS NULL OR p.validTo >= CURRENT_DATE)")
    List<PriceList> findCurrentActivePrices();
    
    List<PriceList> findByActiveTrue();
}
