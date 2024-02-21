package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class BiologicalIndicatorSterilizationDataDeviceDTO {
    private String positionNumber;
    private String sterilizerSerial;
    private String sterilizerName;
    private String dValue;
    private String userName;


    /**
     * Constructor vacío para que Jackson pueda deserializar el JSON
     */
    public BiologicalIndicatorSterilizationDataDeviceDTO() {
    }

    @Override
    public String toString() {
        return "{" +
                "positionNumber='" + positionNumber + '\'' +
                ", sterilizerSerial='" + sterilizerSerial + '\'' +
                ", sterilizerName='" + sterilizerName + '\'' +
                ", dValue='" + dValue + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}