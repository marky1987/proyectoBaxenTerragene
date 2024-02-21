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
@Table(name = "RESULTS_STERILIZATION")
@AllArgsConstructor
@NoArgsConstructor
public class SterilizationEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "results_sterilization_id", nullable = false)
    private Long resultsSterilizationId;

    @Column(name = "position_number")
    private String positionNumber;
    @Column(name = "sterilizer_serial")
    private String sterilizerSerial;
    @Column(name = "sterilizer_name")
    private String sterilizerName;
    @Column(name = "d_value")
    private String dValue;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "ticket_number")
    private String ticketNumber;
    @Column(name = "result")
    private String result;
    @Column(name = "process")
    private String process;
    @Column(name = "condition_SCIB")
    private String conditionSCIB;
    @Column(name = "load_number")
    private String loadNumber;
    @Column(name = "program_number")
    private String programNumber;
    @Column(name = "started_time")
    private String startedTime;
    @Column(name = "average_temperature")
    private String averageTemperature;
    @Column(name = "result_date")
    private String resultDate;
    @Column(name = "incubator_serial_number")
    private String incubatorSerialNumber;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_lot")
    private String productLot;
    @Column(name = "product_brand")
    private String productBrand;
    @Column(name = "incubator_name")
    private String incubatorName;
    @Column(name = "cycle")
    private String cycle;

    @Transient
    private String entity = "RESULTS_STERILIZATION";

    @Override
    public String getModelName() {
        return this.productName;
    }

    @Override
    public String getSerialNumber() {
        return this.sterilizerSerial;
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
