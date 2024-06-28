package com.terragene.baxen.controller;

import com.terragene.baxen.dto.response.HealthCheckResponse;
import com.terragene.baxen.exception.ObtenerTokenException;
import com.terragene.baxen.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/health")
@RequiredArgsConstructor
@CrossOrigin
public class HealthCheckController {

    private static final Logger logger = LogManager.getLogger(HealthCheckController.class);
    private final HealthCheckService healthCheckService;
    private static final int OK = 00;
    private static final int ERROR = 99;


    /*Se haran 2 health check para corroborar el control  del estado de conexion
    *  1 - Health check para verificar la conexion con la base de datos
    * 2 - Health check para verificar la conexion con el servicio de terragene
    *
    * */

    // Health check para verificar la conexion con la base de datos
    @GetMapping(value = "/checkDatabaseConnection")
    public ResponseEntity<HealthCheckResponse> checkDatabaseConnection() {
        HealthCheckResponse response = new HealthCheckResponse();
        boolean result = healthCheckService.checkDatabaseConnection();
        if (result) {
            logger.info("Conexión a la base OK");
            response.setMessage("Conexión a la base OK");
            response.setStatusCode(OK);
        } else {
            logger.error("Error de conexión a la base de datos");
            response.setMessage("Error de conexión a la base de datos");
            response.setStatusCode(ERROR);
        }
        return ResponseEntity.ok(response);
    }

    // Health check para verificar la conexion con el servicio de terragene
    @GetMapping(value = "/checkServiceConnection")
    public ResponseEntity<HealthCheckResponse> checkTerrageneServiceConnection() {
        HealthCheckResponse response = new HealthCheckResponse();
        try {
            boolean result = healthCheckService.checkServiceConnection();
            logger.info("Conexión a Terragene OK");
            if (result) {
                response.setMessage("Conexión a Terragene OK");
                response.setStatusCode(OK);
            } else {
                response.setMessage("Error de conexión a Terragene");
                response.setStatusCode(ERROR);
            }
        } catch (ObtenerTokenException e) {
            logger.error("Error tratando de conectar al servicio de bionova cloud" );
            logger.error(e.getMessage());
            throw new ObtenerTokenException("Error tratando de conectar al servicio de bionova cloud " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
