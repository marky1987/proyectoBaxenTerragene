package com.terragene.baxen.dto.response;

import lombok.Data;

@Data
public class NotificationResponse {
    private String message;
    private Integer statusCode;

    public NotificationResponse(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
    public NotificationResponse() {
    }
}
