package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class BiologicalIndicatorSterilizationDataDTO {
    private String ticketNumber;
    private String result;
    private String process;
    private String conditionSCIB;
    private String loadNumber;
    private String programNumber;
    private String startedTime;
    private String averageTemperature;
    private String resultDate;
    private String incubatorSerialNumber;
    private String productName;
    private String productLot;
    private String productBrand;
    private String incubatorName;
    private String cycle;



    public BiologicalIndicatorSterilizationDataDTO() {
    }

    @Override
    public String toString() {
        return "BiologicalIndicatorSterilizationDataDTO{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", result='" + result + '\'' +
                ", process='" + process + '\'' +
                ", conditionSCIB='" + conditionSCIB + '\'' +
                ", loadNumber='" + loadNumber + '\'' +
                ", programNumber='" + programNumber + '\'' +
                ", startedTime='" + startedTime + '\'' +
                ", averageTemperature='" + averageTemperature + '\'' +
                ", resultDate='" + resultDate + '\'' +
                ", incubatorSerialNumber='" + incubatorSerialNumber + '\'' +
                ", productName='" + productName + '\'' +
                ", productLot='" + productLot + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", incubatorName='" + incubatorName + '\'' +
                ", cycle='" + cycle + '\'' +
                '}';
    }
}
