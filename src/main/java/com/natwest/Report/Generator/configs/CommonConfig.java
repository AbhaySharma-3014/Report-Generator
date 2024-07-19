package com.natwest.Report.Generator.configs;

import com.natwest.Report.Generator.configs.processors.FileProcessor;
import com.natwest.Report.Generator.configs.readers.CsvFileReader;
import com.natwest.Report.Generator.configs.writers.CsvFileWriter;
import com.natwest.Report.Generator.entities.InputData;
import com.natwest.Report.Generator.entities.OutputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfig.class);

    @Autowired
    private ConfigHelper configHelper;

    @Bean
    @StepScope
    public ItemReader<InputData> reader(@Value("#{jobParameters['inputFilePath']}") String inputFilePath) throws Exception {
        if(inputFilePath == null){
            inputFilePath = configHelper.getInputFilePath();
        }
        int lastIndex = inputFilePath.lastIndexOf('.');
        if (lastIndex != -1) {
            String fileExtension = inputFilePath.substring(lastIndex + 1);
            LOGGER.info("Input File extension: " + fileExtension);

            switch (fileExtension){
                case "csv":
                    return getCsvFileReader(inputFilePath);

                case "json":
                case "xls":
                default:
                    throw new Exception("Invalid Input File extension found");
            }
        }
        throw new Exception("No file extension found");
    }

    @Bean
    @StepScope
    public ItemProcessor<InputData, OutputData> itemProcessor() {
        return new FileProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<OutputData> itemWriter(@Value("#{jobParameters['outputFilePath']}") String outputFilePath) throws Exception {

        if(outputFilePath == null){
            outputFilePath = configHelper.getOutputFilePath();
        }

        int lastIndex = outputFilePath.lastIndexOf('.');
        if (lastIndex != -1) {
            String fileExtension = outputFilePath.substring(lastIndex + 1);
            LOGGER.info("Output File extension: " + fileExtension);

            switch (fileExtension){
                case "csv":
                    return getCsvFileWriter(outputFilePath);

                case "json":
                case "xls":
                default:
                    throw new Exception("Invalid Output File extension found");
            }
        }
        throw new Exception("No file extension found");
    }


    public ItemReader<InputData> getCsvFileReader(String inputFilePath) {
        return new CsvFileReader(inputFilePath);
    }

    public ItemWriter<OutputData> getCsvFileWriter(String outputFilePath) {
        return new CsvFileWriter(outputFilePath);
    }

}
