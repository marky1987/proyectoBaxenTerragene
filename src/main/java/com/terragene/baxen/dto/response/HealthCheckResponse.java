package com.terragene.baxen.dto.response;

import lombok.Data;

@Data
public class HealthCheckResponse {
    private String message;
    private Integer statusCode;

    public HealthCheckResponse(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
    public HealthCheckResponse() {
    }
}
