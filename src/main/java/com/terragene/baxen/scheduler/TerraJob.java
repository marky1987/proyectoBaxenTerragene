package com.terragene.baxen.scheduler;


import com.terragene.baxen.component.ApplicationContextProvider;
import com.terragene.baxen.exception.GenericGetRequestException;
import com.terragene.baxen.service.TerraService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

public class TerraJob extends QuartzJobBean {

        private static final Logger logger = LogManager.getLogger(TerraJob.class);

        private TerraService terraService;

        @Override
        protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            logger.info("TerraJob started");
            try {
                    this.terraService = getTerraServiceFromApplicationContext();
                    Map<String, Long>  terraProcess = terraService.processMessage("marky.huevone@gmail.com", "3","Baxen2024#");
                    if (terraProcess.isEmpty()){
                        logger.info("TerraJob finished");
                    } else {
                        terraProcess.forEach((key, value) -> {
                            logger.info(key + ": " + value);
                        });
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
