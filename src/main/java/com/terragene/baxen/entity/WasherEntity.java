package com.terragene.baxen.entity;

import com.terragene.baxen.entity.listeners.AuditListener;
import com.terragene.baxen.entity.listeners.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "results_washer")
public class WasherEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "results_washer_id", nullable = false)
    private Long id;

    @Column(name = "manufacture_indicator")
    private String manufactureIndicator;

    @Column(name = "serial_washer")
    private String serialWasher;

    @Column(name = "brand_washer")
    private String brandWasher;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "manufacture_date")
    private String manufactureDate;

    @Column(name = "washing_time")
    private String washingTime;

    @Column(name = "result")
    private String result;

    @Column(name = "detergent_type")
    private String detergentType;

    @Column(name = "detergent_concentration")
    private String detergentConcentration;

    @Column(name = "location")
    private String location;

    @Column(name = "program")
    private String program;

    @Column(name = "creation_test")
    private String creationTest;

    @Column(name = "temperature")
    private String temperature;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "serial_number_scanner")
    private String serialNumberScanner;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "lot_product")
    private String lotProduct;

    @Column(name = "brand")
    private String brand;

    @Column(name = "cycle")
    private String cycle;

    @Column(name = "water_pressure")
    private String waterPressure;

    @Column(name = "water_hardness")
    private String waterHardness;

    @Transient
    private String entity = "RESULTS_WASHER";

    @Override
    public String getModelName() {
        return this.productName;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumberScanner;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getEntity() {
        return this.entity;
    }
}