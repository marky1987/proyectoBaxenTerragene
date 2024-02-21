package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseSterilizationChemicalIndicatorsDTO {

    @JsonProperty(value = "data_device")
    private SterilizationChemicalIndicatorsDataDeviceDTO device;

    @JsonProperty(value = "data")
    private SterilizationChemicalIndicatorsDataDTO data;

    public ResponseSterilizationChemicalIndicatorsDTO(SterilizationChemicalIndicatorsDataDeviceDTO device, SterilizationChemicalIndicatorsDataDTO data) {
        this.device = device;
        this.data = data;
    }

    public ResponseSterilizationChemicalIndicatorsDTO() {
    }
}
