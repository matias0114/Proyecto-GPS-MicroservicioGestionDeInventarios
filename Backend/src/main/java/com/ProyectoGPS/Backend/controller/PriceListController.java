package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.PriceListDTO;
import com.ProyectoGPS.Backend.model.PriceList;
import com.ProyectoGPS.Backend.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/pricelist")
@CrossOrigin(origins = "*")
public class PriceListController {

    @Autowired
    private PriceListService priceListService;

    @GetMapping
    public ResponseEntity<List<PriceList>> getAllPriceLists() {
        return ResponseEntity.ok(priceListService.getAllPriceLists());
    }

    @GetMapping("/current")
    public ResponseEntity<List<PriceList>> getCurrentPrices() {
        return ResponseEntity.ok(priceListService.getCurrentPrices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceList> getPriceListById(@PathVariable Long id) {
        return priceListService.getPriceListById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PriceList>> getPricesByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(priceListService.getPricesByProductId(productId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<PriceList>> getPricesByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return ResponseEntity.ok(priceListService.getPricesByDate(date));
    }

    @PostMapping
    public ResponseEntity<PriceList> createPriceList(@RequestBody PriceListDTO priceListDTO) {
        return ResponseEntity.ok(priceListService.createPriceListFromDTO(priceListDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceList> updatePriceList(@PathVariable Long id, @RequestBody PriceListDTO priceListDTO) {
        return ResponseEntity.ok(priceListService.updatePriceListFromDTO(id, priceListDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceList(@PathVariable Long id) {
        priceListService.deletePriceList(id);
        return ResponseEntity.ok().build();
    }
}
