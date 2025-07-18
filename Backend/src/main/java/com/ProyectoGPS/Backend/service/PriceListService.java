package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.model.PriceList;
import com.ProyectoGPS.Backend.repository.PriceListRepository;
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
        priceList.setProduct(priceListDetails.getProduct());
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
        return priceListRepository.findByProductId(productId);
    }

    public List<PriceList> getPricesByDate(Date date) {
        return priceListRepository.findActiveByDate(date);
    }
}
