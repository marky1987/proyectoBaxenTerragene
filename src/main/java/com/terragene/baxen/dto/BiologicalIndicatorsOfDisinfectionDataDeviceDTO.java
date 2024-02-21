package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class BiologicalIndicatorsOfDisinfectionDataDeviceDTO {
    private String positionNumber;
    private String disinfectorSerial;
    private String disinfectorName;
    private String peroxideConcentration;
    private String userName;

    public BiologicalIndicatorsOfDisinfectionDataDeviceDTO() {
        // TODO document why this constructor is empty
    }
}
