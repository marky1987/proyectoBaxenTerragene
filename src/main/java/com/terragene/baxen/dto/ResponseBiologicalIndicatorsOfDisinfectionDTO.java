package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseBiologicalIndicatorsOfDisinfectionDTO {
    @JsonProperty(value = "data_device")
    private BiologicalIndicatorsOfDisinfectionDataDeviceDTO device;

    @JsonProperty(value = "data")
    private BiologicalIndicatorsOfDisinfectionDataDTO data;

    public ResponseBiologicalIndicatorsOfDisinfectionDTO() {
    }
}
