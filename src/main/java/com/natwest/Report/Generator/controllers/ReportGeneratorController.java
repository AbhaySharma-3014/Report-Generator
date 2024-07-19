package com.natwest.Report.Generator.controllers;

import com.natwest.Report.Generator.configs.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController("ReportGeneratorController")
@RequestMapping("/generateReport")
public class ReportGeneratorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportGeneratorController.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private ConfigHelper configHelper;

    @RequestMapping(method = RequestMethod.GET)
    public void generateReport() {
        try {
            LOGGER.info("Request received to run Report Generation Job");
            JobParameters jobParameters = new JobParametersBuilder()
                .addDate("runDate", new Date())
                .addString("inputFilePath", configHelper.getInputFilePath())
                .addString("outputFilePath", configHelper.getOutputFilePath())
                .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            LOGGER.info("EXCEPTION OCCURRED - ",e);
            e.printStackTrace();
        }
    }
}