package com.terragene.baxen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "AUDIT_LOGS")
@AllArgsConstructor
@NoArgsConstructor
public class AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private String actionDate;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "nro_serie")
    private String nroSerie;

    @Column(name = "username")
    private String username;


    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
