package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class WashingChemicalIndicatorsDataDeviceDTO {
    private String manufactureIndicator;
    private String serialWasher;
    private String brandWasher;
    private String userName;

    public WashingChemicalIndicatorsDataDeviceDTO() {
        // TODO document why this constructor is empty
    }
}
