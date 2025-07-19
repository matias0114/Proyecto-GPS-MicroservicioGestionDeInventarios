package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.PriceListDTO;
import com.ProyectoGPS.Backend.model.PriceList;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.model.Warehouse;
import com.ProyectoGPS.Backend.repository.PriceListRepository;
import com.ProyectoGPS.Backend.repository.ProductRepository;
import com.ProyectoGPS.Backend.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PriceListService {
    
    @Autowired
    private PriceListRepository priceListRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;

    public List<PriceList> getAllPriceLists() {
        return priceListRepository.findAll();
    }

    public Optional<PriceList> getPriceListById(Long id) {
        return priceListRepository.findById(id);
    }

    @Transactional
    public PriceList createPriceList(PriceList priceList) {
        // Validar fechas
        if (priceList.getValidFrom() == null) {
            priceList.setValidFrom(new Date());
        }
        
        // Si viene un productId pero no el objeto product, buscar el product
        if (priceList.getProduct() == null && priceList.getProductId() != null) {
            Product product = productRepository.findById(priceList.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + priceList.getProductId()));
            priceList.setProduct(product);
        }
        
        // Si viene un warehouseId pero no el objeto warehouse, buscar el warehouse
        if (priceList.getWarehouse() == null && priceList.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(priceList.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada con id: " + priceList.getWarehouseId()));
            priceList.setWarehouse(warehouse);
        }
        
        return priceListRepository.save(priceList);
    }

    @Transactional
    public PriceList updatePriceList(Long id, PriceList priceListDetails) {
        PriceList priceList = priceListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista de precios no encontrada con id: " + id));

        priceList.setName(priceListDetails.getName());
        priceList.setValidFrom(priceListDetails.getValidFrom());
        priceList.setValidTo(priceListDetails.getValidTo());
        priceList.setActive(priceListDetails.getActive());
        
        // Si viene un productId pero no el objeto product, buscar el product
        if (priceListDetails.getProduct() != null) {
            priceList.setProduct(priceListDetails.getProduct());
        } else if (priceListDetails.getProductId() != null) {
            Product product = productRepository.findById(priceListDetails.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + priceListDetails.getProductId()));
            priceList.setProduct(product);
        }
        
        // Si viene un warehouseId pero no el objeto warehouse, buscar el warehouse
        if (priceListDetails.getWarehouse() != null) {
            priceList.setWarehouse(priceListDetails.getWarehouse());
        } else if (priceListDetails.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(priceListDetails.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada con id: " + priceListDetails.getWarehouseId()));
            priceList.setWarehouse(warehouse);
        }
        
        priceList.setSalePrice(priceListDetails.getSalePrice());
        priceList.setCostPrice(priceListDetails.getCostPrice());
        priceList.setMarginPercentage(priceListDetails.getMarginPercentage());
        priceList.setCurrency(priceListDetails.getCurrency());
        priceList.setPriceType(priceListDetails.getPriceType());
        priceList.setPricingMethod(priceListDetails.getPricingMethod());

        return priceListRepository.save(priceList);
    }

    @Transactional
    public void deletePriceList(Long id) {
        PriceList priceList = priceListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista de precios no encontrada con id: " + id));
        priceListRepository.delete(priceList);
    }

    public List<PriceList> getCurrentPrices() {
        return priceListRepository.findCurrentActivePrices();
    }

    public List<PriceList> getPricesByProductId(Long productId) {
        return priceListRepository.findByProduct_Id(productId);
    }

    public List<PriceList> getPricesByDate(Date date) {
        return priceListRepository.findActiveByDate(date);
    }

    // Métodos que usan DTO para mejor manejo de datos desde el frontend
    @Transactional
    public PriceList createPriceListFromDTO(PriceListDTO dto) {
        PriceList priceList = new PriceList();
        mapDTOToPriceList(dto, priceList);
        
        // Validar fechas
        if (priceList.getValidFrom() == null) {
            priceList.setValidFrom(new Date());
        }
        
        return priceListRepository.save(priceList);
    }
    
    @Transactional
    public PriceList updatePriceListFromDTO(Long id, PriceListDTO dto) {
        PriceList priceList = priceListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista de precios no encontrada con id: " + id));
        
        mapDTOToPriceList(dto, priceList);
        
        return priceListRepository.save(priceList);
    }
    
    private void mapDTOToPriceList(PriceListDTO dto, PriceList priceList) {
        priceList.setName(dto.getName());
        priceList.setValidFrom(dto.getValidFrom());
        priceList.setValidTo(dto.getValidTo());
        priceList.setActive(dto.getActive());
        priceList.setSalePrice(dto.getSalePrice());
        priceList.setCostPrice(dto.getCostPrice());
        priceList.setMarginPercentage(dto.getMarginPercentage());
        priceList.setCurrency(dto.getCurrency());
        priceList.setPriceType(dto.getPriceType());
        priceList.setPricingMethod(dto.getPricingMethod());
        
        // Buscar y establecer el producto
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + dto.getProductId()));
            priceList.setProduct(product);
        }
        
        // Buscar y establecer la bodega (si se especifica)
        if (dto.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada con id: " + dto.getWarehouseId()));
            priceList.setWarehouse(warehouse);
        }
    }
}
