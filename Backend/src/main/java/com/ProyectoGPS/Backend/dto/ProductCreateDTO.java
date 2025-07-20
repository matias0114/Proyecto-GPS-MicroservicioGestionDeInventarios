package com.ProyectoGPS.Backend.dto;

import com.ProyectoGPS.Backend.model.Product;

import java.math.BigDecimal;

public class ProductCreateDTO {
    private String code;
    private String name;
    private String description;
    private String category;
    private String presentation;
    private String laboratory;
    private Product.PricingMethod defaultPricingMethod;
    private BigDecimal basePrice;
    private String unit;
    private Boolean active;

    // Constructor vacío
    public ProductCreateDTO() {}

    // Getters y Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public Product.PricingMethod getDefaultPricingMethod() {
        return defaultPricingMethod;
    }

    public void setDefaultPricingMethod(Product.PricingMethod defaultPricingMethod) {
        this.defaultPricingMethod = defaultPricingMethod;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // Método para convertir DTO a Product
    public Product toProduct() {
        Product product = new Product();
        product.setCode(this.code);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setCategory(this.category);
        product.setPresentation(this.presentation);
        product.setLaboratory(this.laboratory);
        product.setDefaultPricingMethod(this.defaultPricingMethod != null ? this.defaultPricingMethod : Product.PricingMethod.LAST_PURCHASE);
        product.setBasePrice(this.basePrice);
        product.setUnit(this.unit);
        product.setActive(this.active != null ? this.active : true);
        return product;
    }
}
