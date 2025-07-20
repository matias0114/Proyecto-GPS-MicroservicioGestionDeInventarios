package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.ProductCreateDTO;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    // Obtener todos los productos activos
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener producto por código
    @GetMapping("/code/{code}")
    public ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        return productService.getProductByCode(code)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }

    // Búsqueda de productos
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        List<Product> products = productService.searchProducts(q);
        return ResponseEntity.ok(products);
    }

    // Productos por categoría
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    // Productos con stock
    @GetMapping("/with-stock")
    public ResponseEntity<List<Product>> getProductsWithStock() {
        List<Product> products = productService.getProductsWithStock();
        return ResponseEntity.ok(products);
    }

    // Productos sin stock
    @GetMapping("/without-stock")
    public ResponseEntity<List<Product>> getProductsWithoutStock() {
        List<Product> products = productService.getProductsWithoutStock();
        return ResponseEntity.ok(products);
    }

    // Obtener categorías disponibles
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = productService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }

    // Obtener laboratorios disponibles
    @GetMapping("/laboratories")
    public ResponseEntity<List<String>> getLaboratories() {
        List<String> laboratories = productService.getAvailableLaboratories();
        return ResponseEntity.ok(laboratories);
    }

    // Crear producto
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDTO productDTO) {
        try {
            // Validaciones básicas
            if (productDTO.getCode() == null || productDTO.getCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El código del producto es obligatorio");
            }
            if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del producto es obligatorio");
            }
            
            // Convertir DTO a Product
            Product product = productDTO.toProduct();
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Activar/Desactivar producto
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleProductStatus(@PathVariable Long id) {
        try {
            Product updatedProduct = productService.toggleProductStatus(id);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar precio base
    @PatchMapping("/{id}/price")
    public ResponseEntity<?> updateProductPrice(@PathVariable Long id, @RequestBody BigDecimal newPrice) {
        try {
            Product updatedProduct = productService.updateBasePrice(id, newPrice);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Verificar si existe código
    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        boolean exists = productService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }

    // Endpoint de prueba para verificar que el servidor recibe JSON correctamente
    @PostMapping("/test")
    public ResponseEntity<?> testEndpoint(@RequestBody ProductCreateDTO productDTO) {
        return ResponseEntity.ok()
                .body("✅ Servidor recibió correctamente: " + productDTO.getName() + 
                      " con código: " + productDTO.getCode());
    }
}
