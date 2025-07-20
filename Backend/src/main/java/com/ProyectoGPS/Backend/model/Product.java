package com.ProyectoGPS.Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;
    private String category;
    private String presentation;
    private String laboratory;
    
    // Configuración de pricing
    @Enumerated(EnumType.STRING)
    private PricingMethod defaultPricingMethod = PricingMethod.LAST_PURCHASE;
    
    private BigDecimal basePrice;
    private String unit; // Unidad de medida
    
    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("product-batches")
    @JsonIgnoreProperties({"product"})
    private List<Batch> batches;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("product-pricelists")
    @JsonIgnoreProperties({"product"})
    private List<PriceList> priceLists;

    public enum PricingMethod {
        LAST_PURCHASE("Última Compra"),
        WEIGHTED_AVERAGE("Promedio Ponderado"),
        FIFO("FIFO - Primero en Entrar, Primero en Salir"),
        LIFO("LIFO - Último en Entrar, Primero en Salir"),
        LILO("LILO - Último en Entrar, Último en Salir");

        private final String description;

        PricingMethod(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
