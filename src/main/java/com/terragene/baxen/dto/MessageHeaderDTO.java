package com.terragene.baxen.dto;

import lombok.Data;

@Data
public class MessageHeaderDTO {
    String fieldSeparator;
    String encodingCharacters;
    String dateTimeOfMessage;
    String messageCode;
    String triggerEvent;
    String messageControlId;
    String processingId;
    String versionId;

    /**
     * Constructor vacío para que Jackson pueda deserializar el JSON
     */
    public MessageHeaderDTO() {

    }

    @Override
    public String toString() {
        return "{" +
                "fieldSeparator='" + fieldSeparator + '\'' +
                ", encodingCharacters='" + encodingCharacters + '\'' +
                ", dateTimeOfMessage='" + dateTimeOfMessage + '\'' +
                ", messageCode='" + messageCode + '\'' +
                ", triggerEvent='" + triggerEvent + '\'' +
                ", messageControlId='" + messageControlId + '\'' +
                ", processingId='" + processingId + '\'' +
                ", versionId='" + versionId + '\'' +
                '}';
    }
}
