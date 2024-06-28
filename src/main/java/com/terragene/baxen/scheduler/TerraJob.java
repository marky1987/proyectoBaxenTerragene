package com.terragene.baxen.scheduler;


import com.terragene.baxen.component.ApplicationContextProvider;
import com.terragene.baxen.entity.UsersEntity;
import com.terragene.baxen.exception.GenericGetRequestException;
import com.terragene.baxen.repository.UsersRepository;
import com.terragene.baxen.service.TerraService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TerraJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(TerraJob.class);

    private final UsersRepository usersRepository;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.info("==========Inicializa el job de Baxen==========");
        try {
            Optional<UsersEntity> usersEntity = usersRepository.getAll();
            TerraService terraService = getTerraServiceFromApplicationContext();
            // validar si usersEntity es null o vacio
            if (usersEntity.isEmpty()) {
                logger.error("No se encontraron usuarios");
                throw new GenericGetRequestException("No se encontraron usuarios");
            } else {
                logger.info("==========Inicia el proceso de Baxen de registros en estado 2==========");
                Map<String, Long> terraProcessStatusRead2 = terraService.processMessage(usersEntity.get().getUsername(), "2", usersEntity.get().getPassword());
                if (terraProcessStatusRead2.isEmpty()) {
                    logger.info("==========Finaliza el proceso de Baxen de registros en estado 2==========");
                } else {
                    terraProcessStatusRead2.forEach((key, value) -> logger.info(String.format("%s: %s", key, value)));
                }
                logger.info("==========Inicia el proceso de Baxen de registros en estado 3==========");
                Map<String, Long> terraProcessStatusRead3 = terraService.processMessage(usersEntity.get().getUsername(), "3", usersEntity.get().getPassword());
                if (terraProcessStatusRead3.isEmpty()) {
                    logger.info("==========Finaliza el proceso de Baxen de registro en estado 3==========");
                } else {
                    terraProcessStatusRead3.forEach((key, value) -> logger.info(String.format("%s: %s", key, value)));
                }
            }

        } catch (Exception e) {
            logger.error("Error en TerraJob", e);
            throw new GenericGetRequestException("Error en TerraJob");
        }
    }

    private TerraService getTerraServiceFromApplicationContext() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(TerraService.class);
    }
}
