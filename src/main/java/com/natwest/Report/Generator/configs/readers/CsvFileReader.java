package com.natwest.Report.Generator.configs.readers;

import com.natwest.Report.Generator.entities.InputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CsvFileReader implements FileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileReader.class);

    public FlatFileItemReader<InputData> reader;

    public CsvFileReader(String inputFilePath) {
        buildReader(inputFilePath);
    }

    @Override
    public InputData read() throws Exception {
        return reader.read();
    }

    @Override
    public void buildReader(String inputFilePath){
        LOGGER.info("Building Reader");
        String[] fields = Arrays.stream(InputData.class.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);

        LOGGER.info("Total Input Fields : {}", fields.length);

        reader = new FlatFileItemReaderBuilder<InputData>()
                .name("csvFileReader")
                .resource(new FileSystemResource(inputFilePath))
                .linesToSkip(1)
                .delimited()
                .names(fields)
                .targetType(InputData.class)
                .build();
        reader.open(new ExecutionContext());
    }
}
