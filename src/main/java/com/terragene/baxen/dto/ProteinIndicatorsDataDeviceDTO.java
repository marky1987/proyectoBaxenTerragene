package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class ProteinIndicatorsDataDeviceDTO {
    private String positionNumber;
    private String washerSerial;
    private String washerName;
    private String protein;
    private String userName;


    public ProteinIndicatorsDataDeviceDTO() {
        // TODO document why this constructor is empty
    }
}
