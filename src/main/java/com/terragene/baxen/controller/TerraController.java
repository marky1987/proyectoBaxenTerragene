package com.terragene.baxen.controller;


import com.terragene.baxen.dto.GenericDTO;
import com.terragene.baxen.dto.LoginDTO;
import com.terragene.baxen.dto.TokenDTO;
import com.terragene.baxen.service.TerraService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class TerraController {

    private final TerraService terraService;


    @GetMapping(value = "/obtenerNovedades")
    public ResponseEntity<String> processMessage(@RequestHeader("user") String username, @RequestHeader("status_read") String statusRead, @RequestHeader("password") String password) throws IOException, JSONException, ParseException {
        Map<String, Long> resultado  = terraService.processMessage(username, statusRead, password);
        if (resultado.isEmpty()) {
            return new ResponseEntity<>("No hay novedades", HttpStatus.OK);
        } else {
            /*Se sumariza la cantidad de registros*/
            long total = resultado.values().stream().mapToLong(Long::longValue).sum();

            if (total == 1) {
                return new ResponseEntity<>("Se procesa " + total + " novedad", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Se procesan " + total + " novedades", HttpStatus.OK);
            }
        }
    }


    @PostMapping(value = "/token")
    public TokenDTO tokenLogin(@RequestBody LoginDTO loginCredentials) {
        return terraService.tokenLogin(loginCredentials);
    }

    @GetMapping(value = "/biSterilizationResults", produces = MediaType.APPLICATION_JSON_VALUE)
    public String biSterilizationResult(@RequestHeader(value = "Authorization") String token, @RequestBody GenericDTO genericDTO) throws JSONException, ParseException {
        return terraService.getBiSterilizationResult(token, genericDTO);
    }

    @GetMapping(value = "/biDisinfectionResults", produces = MediaType.APPLICATION_JSON_VALUE)
    public String biDisinfectionResult(@RequestHeader(value = "Authorization") String token, @RequestBody GenericDTO genericDTO) throws JSONException,  ParseException {
        return terraService.getBiDisinfectionResults(token, genericDTO);
    }

    @GetMapping(value = "/proResults", produces = MediaType.APPLICATION_JSON_VALUE)
    public String proResult(@RequestHeader(value = "Authorization") String token, @RequestBody GenericDTO genericDTO) throws JSONException,  ParseException {
        return terraService.getProResults(token, genericDTO);
    }

    @GetMapping(value = "/washerResults", produces = MediaType.APPLICATION_JSON_VALUE)
    public String washerResult(@RequestHeader(value = "Authorization") String token, @RequestBody GenericDTO genericDTO) throws JSONException,  ParseException {
        return terraService.getWasherResults(token, genericDTO);
    }

    @GetMapping(value = "/sterilizerResults", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sterilizerResult(@RequestHeader(value = "Authorization") String token, @RequestBody GenericDTO genericDTO) throws JSONException,  ParseException {
        return terraService.getSterilizerResults(token, genericDTO);
    }

}
