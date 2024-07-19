package com.natwest.Report.Generator.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

@Service
public class JobNotificationService implements JobExecutionListener {

    private final Logger LOGGER = LoggerFactory.getLogger(JobNotificationService.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("Job has been started. Please wait until it get completely executed.");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(BatchStatus.COMPLETED.equals(jobExecution.getStatus())){
            LOGGER.info("Job has been successfully executed. Please go through the output.csv file");
        }
    }
}