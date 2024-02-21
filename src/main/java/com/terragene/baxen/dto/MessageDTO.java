package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageDTO {
    @JsonProperty("Position")
    String position;
    @JsonProperty("IncubatorSerialNumber")
    String incubatorSerialNumber;


}
