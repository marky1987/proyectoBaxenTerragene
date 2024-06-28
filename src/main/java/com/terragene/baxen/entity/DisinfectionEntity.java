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
@Table(name = "RESULTS_DISINFECTION")
@AllArgsConstructor
@NoArgsConstructor
public class DisinfectionEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "results_disinfection_id", nullable = false)
    private Long resultsDisinfectionId;

    @Column(name = "position_number")
    private String positionNumber;
    @Column(name = "disinfector_serial")
    private String disinfectorSerial;

    @Column(name = "disinfector_name")
    private String disinfectorName;
    @Column(name = "peroxide_concentration")
    private String peroxideConcentration;
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
    @Column(name = "room_id")
    private String roomId;
    @Column(name = "room_volume")
    private String roomVolume;
    @Column(name = "started_time")
    private String startedTime;
    @Column(name = "average_temperature")
    private String averageTemperature;
    @Column(name = "result_date")
    private String resultDate;
    @Column(name = "incubator_serial_number")
    private String incubatorSerialNumber;
    @Column(name = "incubator_name")
    private String productName;
    @Column(name = "product_lot")
    private String productLot;
    @Column(name = "product_brand")
    private String productBrand;
    @Column(name = "product_model")
    private String incubatorName;

    @Transient
    private String entity = "RESULTS_DISINFECTION";

    @Override
    public String getModelName() {
        return this.productBrand;
    }

    @Override
    public String getSerialNumber() {
        return this.incubatorSerialNumber;
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
