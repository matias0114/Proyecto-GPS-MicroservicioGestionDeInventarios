package com.ProyectoGPS.Backend.dto;

import com.ProyectoGPS.Backend.model.PriceList;
import com.ProyectoGPS.Backend.model.Product;

import java.math.BigDecimal;
import java.util.Date;

public class PriceListDTO {
    private Long id;
    private String name;
    private Long productId;
    private Long warehouseId;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private BigDecimal marginPercentage;
    private Date validFrom;
    private Date validTo;
    private Boolean active;
    private PriceList.PriceType priceType;
    private String currency;
    private Product.PricingMethod pricingMethod;

    // Constructor vacío
    public PriceListDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getMarginPercentage() {
        return marginPercentage;
    }

    public void setMarginPercentage(BigDecimal marginPercentage) {
        this.marginPercentage = marginPercentage;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public PriceList.PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceList.PriceType priceType) {
        this.priceType = priceType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Product.PricingMethod getPricingMethod() {
        return pricingMethod;
    }

    public void setPricingMethod(Product.PricingMethod pricingMethod) {
        this.pricingMethod = pricingMethod;
    }
}
