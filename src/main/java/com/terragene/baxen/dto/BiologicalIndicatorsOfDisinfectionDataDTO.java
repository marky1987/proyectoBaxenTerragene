package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class BiologicalIndicatorsOfDisinfectionDataDTO {
    private String ticketNumber;
    private String result;
    private String process;
    private String conditionSCIB;
    private String roomId;
    private String roomVolume;
    private String startedTime;
    private String averageTemperature;
    private String resultDate;
    private String incubatorSerialNumber;
    private String productName;
    private String productLot;
    private String productBrand;
    private String incubatorName;
}
