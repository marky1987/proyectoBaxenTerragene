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
@Table(name = "RESULTS_PROTEIN")
@AllArgsConstructor
@NoArgsConstructor
public class ProteinEntity  implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "results_protein_id", nullable = false)
    private Long proteinResultsId;

    @Column(name = "position_number")
    private String positionNumber;
    @Column(name = "washer_serial")
    private String washerSerial;
    @Column(name = "washer_name")
    private String washerName;
    @Column(name = "protein")
    private String protein;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "ticket_number")
    private String ticketNumber;
    @Column(name = "result")
    private String result;
    @Column(name = "surface")
    private String surface;
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
    @Column(name = "lote_product")
    private String loteProduct;
    @Column(name = "product_brand")
    private String productBrand;
    @Column(name = "incubator_name")
    private String incubatorName;
    @Column(name = "cycle")
    private String cycle;

    @Transient
    private String entity = "RESULTS_PROTEIN";

    @Override
    public String getModelName() {
        return this.incubatorName;
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
