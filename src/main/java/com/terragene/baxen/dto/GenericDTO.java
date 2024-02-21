package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GenericDTO {

    @JsonProperty("From")
    private String from;
    @JsonProperty("To")
    private String to;
    @JsonProperty("StatusRead")
    private String statusRead;

    public GenericDTO(String from, String to, String statusRead) {
        this.from = from;
        this.to = to;
        this.statusRead = statusRead;
    }

    @Override
    public String toString() {
        return "{" +
                "From:'" + from + '\'' +
                ", To: '" + to + '\'' +
                ", StatusRead:'" + statusRead + '\'' +
                '}';
    }
}
