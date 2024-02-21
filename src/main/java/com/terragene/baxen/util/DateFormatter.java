package com.terragene.baxen.util;


import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final String INPUT_FORMAT = "yyyyMMddHHmmss";
    private static final String OUTPUT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateFormatter() {
    }



    public static String format(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormat.forPattern(INPUT_FORMAT);
        DateTimeFormatter outputFormatter = DateTimeFormat.forPattern(OUTPUT_FORMAT);
        String formattedDate = null;

        try {
            LocalDateTime localDateTime = inputFormatter.parseLocalDateTime(dateString);
            formattedDate = localDateTime.toString(outputFormatter);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al analizar la cadena de fecha: " + e.getMessage());
        }

        return formattedDate;
    }



    public static LocalTime parseToLocalTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmmss");
        LocalTime localTime = null;

        try {
            localTime = formatter.parseLocalTime(timeString);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al analizar la cadena de tiempo: " + e.getMessage());
        }

        return localTime;
    }

    public static String formatTime(String timeString) {
        DateTimeFormatter inputFormatter = DateTimeFormat.forPattern("HH:mm:ss.SSS");
        DateTimeFormatter outputFormatter = DateTimeFormat.forPattern("HH:mm:ss");
        String formattedTime = null;

        try {
            LocalTime localTime = inputFormatter.parseLocalTime(timeString);
            formattedTime = localTime.toString(outputFormatter);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al analizar la cadena de tiempo: " + e.getMessage());
        }

        return formattedTime;
    }
}
