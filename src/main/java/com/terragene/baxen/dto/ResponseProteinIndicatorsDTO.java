package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseProteinIndicatorsDTO {
    @JsonProperty(value = "data_device")
    private ProteinIndicatorsDataDeviceDTO device;

    @JsonProperty(value = "data")
    private ProteinIndicatorsDataDTO data;

    public ResponseProteinIndicatorsDTO() {
    }

    public ResponseProteinIndicatorsDTO(ProteinIndicatorsDataDeviceDTO device, ProteinIndicatorsDataDTO data) {
        this.device = device;
        this.data = data;
    }
}
