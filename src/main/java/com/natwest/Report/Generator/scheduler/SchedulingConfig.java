package com.natwest.Report.Generator.scheduler;

import com.natwest.Report.Generator.configs.ConfigHelper;
import com.natwest.Report.Generator.controllers.ReportGeneratorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SchedulingConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingConfig.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private ConfigHelper configHelper;

    @Scheduled(cron = "#{@configHelper.getCronExpression()}")
    public void runJob() {
        try {
            LOGGER.info("As per Schedule, its time to Run the Report Generation Job");
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addString("inputFilePath", configHelper.getInputFilePath());
            jobParametersBuilder.addString("outputFilePath", configHelper.getOutputFilePath());
            jobParametersBuilder.addDate("runDate", new Date());
            jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

