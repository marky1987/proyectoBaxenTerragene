package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class SterilizationChemicalIndicatorsDataDeviceDTO {
    private String manufactureIndicator;
    private String serialSterilizer;
    private String brandSterilizer;
    private String userName;

    public SterilizationChemicalIndicatorsDataDeviceDTO() {
        // TODO document why this constructor is empty
    }
}
