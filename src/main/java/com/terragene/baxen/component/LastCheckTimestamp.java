package com.terragene.baxen.component;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Component
public class LastCheckTimestamp {
    // Inicializar a una fecha antigua para la primera contabilización
    private LocalDateTime lastChecked = LocalDateTime.of(1970, 1, 1, 0, 0);

    public void updateLastChecked(LocalDateTime dateTime) {
        this.lastChecked = dateTime;
    }

    // Métodos utilitarios para convertir entre Date y LocalDateTime si es necesario
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}