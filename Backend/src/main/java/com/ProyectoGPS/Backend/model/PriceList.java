package com.ProyectoGPS.Backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "price_lists")
public class PriceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("product-pricelists")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse; // Precio específico por bodega

    @Column(nullable = false)
    private BigDecimal salePrice; // Precio de venta
    
    private BigDecimal costPrice; // Precio de costo
    private BigDecimal marginPercentage; // Margen de ganancia en porcentaje
    
    @Column(nullable = false)
    private Date validFrom;
    
    private Date validTo;
    
    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    private PriceType priceType = PriceType.GENERAL;

    private String currency = "CLP";

    @Enumerated(EnumType.STRING)
    private Product.PricingMethod pricingMethod; // Método usado para calcular este precio

    public enum PriceType {
        GENERAL("General"),
        WHOLESALE("Mayorista"),
        RETAIL("Minorista"),
        GOVERNMENT("Gubernamental"),
        SPECIAL("Especial");

        private final String description;

        PriceType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
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

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
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
