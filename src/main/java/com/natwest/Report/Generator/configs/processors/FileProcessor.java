package com.natwest.Report.Generator.configs.processors;

import com.natwest.Report.Generator.entities.InputData;
import com.natwest.Report.Generator.entities.OutputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

public class FileProcessor implements ItemProcessor<InputData, OutputData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);

    @Autowired
    private ProcessorHelper processorHelper;

    @Value("${reference.file.path}")
    private String referenceFilePath;

    @Override
    public OutputData process(InputData item) throws Exception {
        if(Objects.nonNull(item)){
            OutputData outputData = processorHelper.getOutputData(item, referenceFilePath);
            return outputData;
        }else{
            LOGGER.error("EXCEPTION OCCURRED - No Input Data Found");
            throw new Exception("No Input Data Found");
        }
    }
}