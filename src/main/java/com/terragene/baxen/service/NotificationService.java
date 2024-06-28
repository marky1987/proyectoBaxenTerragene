package com.terragene.baxen.service;

import com.terragene.baxen.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

        private static final Logger logger = LogManager.getLogger(NotificationService.class);

        private final AuditRepository auditRepository;

        public Boolean obtenerNotificaciones() {
                boolean result = false;
                // validamos si el resultado es > a 0 en caso de que si sea mayor a 0 entonces antes de retornar true actualizamos los registros que estan en 0 y los pasamos a 1
                if (auditRepository.hasNewNotification() > 0) {
                        logger.info("Se encontraron notificaciones nuevas");
                        auditRepository.updateNotifications();
                        result = true;
                }
                return result;
        }
}
