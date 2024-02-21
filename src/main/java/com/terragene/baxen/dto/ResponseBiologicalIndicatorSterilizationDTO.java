package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseBiologicalIndicatorSterilizationDTO {

    @JsonProperty(value = "data_device")
    private BiologicalIndicatorSterilizationDataDeviceDTO device;

    @JsonProperty(value = "data")
    private BiologicalIndicatorSterilizationDataDTO data;

    public ResponseBiologicalIndicatorSterilizationDTO() {
    }

    public ResponseBiologicalIndicatorSterilizationDTO(BiologicalIndicatorSterilizationDataDeviceDTO device, BiologicalIndicatorSterilizationDataDTO data) {
        this.device = device;
        this.data = data;
    }
}
