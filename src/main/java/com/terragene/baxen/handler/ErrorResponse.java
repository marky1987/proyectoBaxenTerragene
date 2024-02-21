package com.terragene.baxen.handler;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private int statusCode;
    private String statusReason;

    public ErrorResponse(String message, int statusCode, String statusReason) {
        this.message = message;
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }

    public ErrorResponse() {
    }


}
