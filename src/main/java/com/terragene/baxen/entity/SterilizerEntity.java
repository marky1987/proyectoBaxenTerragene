package com.terragene.baxen.entity;

import com.terragene.baxen.entity.listeners.AuditListener;
import com.terragene.baxen.entity.listeners.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "RESULTS_STERILIZER")
@AllArgsConstructor
@NoArgsConstructor
public class SterilizerEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "results_sterilizer_id", nullable = false)
    private int resultsSterilizerId;

    @Column(name = "manufacture_indicator")
    private String manufactureIndicator;

    @Column(name = "serial_sterilizer")
    private String serialSterilizer;

    @Column(name = "brand_sterilizer")
    private String brandSterilizer;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "manufacture_date")
    private String manufactureDate;

    @Column(name = "exposure_time")
    private String exposureTime;

    @Column(name = "result")
    private String result;

    @Column(name = "concentration")
    private String concentration;

    @Column(name = "relative_dampness")
    private String relativeDampness;

    @Column(name = "package_number")
    private String packageNumber;

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

    @Column(name = "name_product")
    private String nameProduct;

    @Column(name = "lote_product")
    private String loteProduct;

    @Column(name = "brand")
    private String brand;

    @Column(name = "cycle")
    private String cycle;

    @Transient
    private String entity = "RESULTS_STERILIZER";

    @Override
    public String getModelName() {
        return this.nameProduct;
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
