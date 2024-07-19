package com.natwest.Report.Generator.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    @Value("${input.file.path}")
    private String inputFilePath;

    @Value("${output.file.path}")
    private String outputFilePath;

    @Value("${cron.expression}")
    private String cronExpression;

    @Value("${chunk.size}")
    private String chunkSize;

    public String getInputFilePath() {
        LOGGER.info("inputFilePath : " + inputFilePath);
        return inputFilePath;
    }

    public String getOutputFilePath() {
        LOGGER.info("outputFilePath : " + outputFilePath);
        return outputFilePath;
    }

    public String getCronExpression() {
        LOGGER.info("cronExpression : " + cronExpression);
        return cronExpression;
    }

    public String getChunkSize() {
        LOGGER.info("chunkSize : " + chunkSize);
        return chunkSize;
    }
}

