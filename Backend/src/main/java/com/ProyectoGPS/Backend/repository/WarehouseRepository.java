package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findByName(String name);
}
