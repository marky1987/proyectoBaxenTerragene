package com.terragene.baxen.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terragene.baxen.dto.MyObjectDTO;
import com.terragene.baxen.dto.response.NotificationResponse;
import com.terragene.baxen.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/notification")
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {

    private static final Logger logger = LogManager.getLogger(NotificationController.class);

    private static final int OK = 00;
    private static final int NO_CONTENT = 99;
    private final NotificationService notificationService;

    // Consultamos si hay novedades
    @GetMapping(value = "/obtenerNovedades")
    public ResponseEntity<NotificationResponse> getNotifications() {
        boolean result = notificationService.obtenerNotificaciones();
        NotificationResponse notificationResponse = new NotificationResponse();
        if (result) {
            logger.info("Novedades encontradas");
            notificationResponse.setMessage("Novedades encontradas");
            notificationResponse.setStatusCode(OK);
        } else {
            logger.info("No hay novedades");
            notificationResponse.setMessage("No hay novedades");
            notificationResponse.setStatusCode(NO_CONTENT);

        }
        return ResponseEntity.ok(notificationResponse);
    }

    @PostMapping(value = "/procesarNovedades")
    public ResponseEntity<NotificationResponse> processNotifications(@RequestBody  String novedad) {

     /*   for (MyObjectDTO myObjectDTO : novedad) {
            logger.info("Novedad: " + myObjectDTO.toString());
        }*/
        novedad = novedad.replace("[", "").replace("]", "");
        System.out.println(novedad);

        return ResponseEntity.ok().build();
    }
}
