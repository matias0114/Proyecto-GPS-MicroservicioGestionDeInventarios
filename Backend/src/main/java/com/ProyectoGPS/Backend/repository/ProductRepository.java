package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Búsqueda por código único
    Optional<Product> findByCode(String code);
    
    // Verificar si existe un código
    boolean existsByCode(String code);
    
    // Productos activos
    List<Product> findByActiveTrue();
    
    // Búsqueda por categoría
    List<Product> findByCategoryAndActiveTrue(String category);
    
    // Búsqueda por nombre (case insensitive)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.active = true")
    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(@Param("name") String name);
    
    // Búsqueda por laboratorio
    List<Product> findByLaboratoryAndActiveTrue(String laboratory);
    
    // Búsqueda general (código, nombre o descripción)
    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND p.active = true")
    List<Product> searchProducts(@Param("search") String search);
    
    // Productos con batches activos
    @Query("SELECT DISTINCT p FROM Product p JOIN p.batches b WHERE b.quantity > 0 AND p.active = true")
    List<Product> findProductsWithStock();
    
    // Productos sin stock
    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(SELECT COALESCE(SUM(b.quantity), 0) FROM Batch b WHERE b.product = p) = 0")
    List<Product> findProductsWithoutStock();
    
    // Productos por categorías específicas del inventario
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.active = true ORDER BY p.category")
    List<String> findDistinctCategories();
    
    // Productos por laboratorio
    @Query("SELECT DISTINCT p.laboratory FROM Product p WHERE p.active = true AND p.laboratory IS NOT NULL ORDER BY p.laboratory")
    List<String> findDistinctLaboratories();
}
