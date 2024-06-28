package com.terragene.baxen.service;

import com.terragene.baxen.dto.LoginDTO;
import com.terragene.baxen.entity.UsersEntity;
import com.terragene.baxen.repository.AuditRepository;
import com.terragene.baxen.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final AuditRepository auditRepository;
    private final UsersRepository usersRepository;
    private final TerraService terraService;

    /*Se haran 2 health check para corroborar el control  del estado de conexion
     *  1 - Health check para verificar la conexion con la base de datos
     * 2 - Health check para verificar la conexion con el servicio de terragene
     *
     * */

    public boolean checkDatabaseConnection() {
        auditRepository.count();
        return true;
    }

    public boolean checkServiceConnection() {
        boolean estadoConexion = false;
        Optional<UsersEntity> usersEntity = usersRepository.getAll();
        if (usersEntity.isEmpty()) {
            return false;
        }
        if (terraService.tokenLogin(new LoginDTO(usersEntity.get().getUsername(), usersEntity.get().getPassword(), usersEntity.get().getDomainDist())) != null) {
            estadoConexion = true;
        }
        return estadoConexion;
    }

}
