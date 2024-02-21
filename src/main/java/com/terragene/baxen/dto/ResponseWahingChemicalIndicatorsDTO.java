package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseWahingChemicalIndicatorsDTO {
    @JsonProperty(value = "data_device")
    private WashingChemicalIndicatorsDataDeviceDTO device;

    @JsonProperty(value = "data")
    private WashingChemicalIndicatorsDataDTO data;

    public ResponseWahingChemicalIndicatorsDTO(WashingChemicalIndicatorsDataDeviceDTO device, WashingChemicalIndicatorsDataDTO data) {
        this.device = device;
        this.data = data;
    }

    public ResponseWahingChemicalIndicatorsDTO() {
    }
}
