package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // Crear producto
    public Product createProduct(Product product) {
        if (productRepository.existsByCode(product.getCode())) {
            throw new RuntimeException("Ya existe un producto con el código: " + product.getCode());
        }
        return productRepository.save(product);
    }

    // Obtener todos los productos activos
    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Buscar por código
    @Transactional(readOnly = true)
    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    // Búsqueda general
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String search) {
        return productRepository.searchProducts(search);
    }

    // Productos por categoría
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    // Productos con stock
    @Transactional(readOnly = true)
    public List<Product> getProductsWithStock() {
        return productRepository.findProductsWithStock();
    }

    // Productos sin stock
    @Transactional(readOnly = true)
    public List<Product> getProductsWithoutStock() {
        return productRepository.findProductsWithoutStock();
    }

    // Obtener categorías disponibles
    @Transactional(readOnly = true)
    public List<String> getAvailableCategories() {
        return productRepository.findDistinctCategories();
    }

    // Obtener laboratorios disponibles
    @Transactional(readOnly = true)
    public List<String> getAvailableLaboratories() {
        return productRepository.findDistinctLaboratories();
    }

    // Actualizar producto
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    // Verificar código único solo si cambió
                    if (!product.getCode().equals(productDetails.getCode()) && 
                        productRepository.existsByCode(productDetails.getCode())) {
                        throw new RuntimeException("Ya existe un producto con el código: " + productDetails.getCode());
                    }
                    
                    product.setCode(productDetails.getCode());
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setCategory(productDetails.getCategory());
                    product.setPresentation(productDetails.getPresentation());
                    product.setLaboratory(productDetails.getLaboratory());
                    product.setBasePrice(productDetails.getBasePrice());
                    product.setUnit(productDetails.getUnit());
                    product.setDefaultPricingMethod(productDetails.getDefaultPricingMethod());
                    product.setActive(productDetails.getActive());
                    
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // Activar/Desactivar producto
    public Product toggleProductStatus(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(!product.getActive());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // Actualizar precio base
    public Product updateBasePrice(Long id, BigDecimal newPrice) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setBasePrice(newPrice);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // Verificar si existe por código
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }
}
