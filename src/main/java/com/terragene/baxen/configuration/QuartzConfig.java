package com.terragene.baxen.configuration;


import com.terragene.baxen.scheduler.TerraJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${baxen.cron.expression}")
    private String cronSchedule;
    @Bean
    public JobDetail apiJobDetail() {
        return JobBuilder.newJob(TerraJob.class)
                .withIdentity("TerraJobApp")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger apiJobTrigger(JobDetail apiJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(apiJobDetail)
                .withIdentity("APIJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                .build();
    }
}
