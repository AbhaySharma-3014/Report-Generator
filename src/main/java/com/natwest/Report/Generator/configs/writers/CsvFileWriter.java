package com.natwest.Report.Generator.configs.writers;

import com.natwest.Report.Generator.entities.OutputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CsvFileWriter implements FileWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileWriter.class);

    private FlatFileItemWriter<OutputData> csvFileWriter;

    public CsvFileWriter(String outputFilePath) {
        buildWriter(outputFilePath);
    }

    @Override
    public void write(Chunk<? extends OutputData> chunk) throws Exception {
        csvFileWriter.write(chunk);
    }

    @Override
    public void buildWriter(String outputFilePath) {
        LOGGER.info("Building Writer");
        String[] fields = Arrays.stream(OutputData.class.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);

        LOGGER.info("Total Output Fields : {}", fields.length);

        BeanWrapperFieldExtractor<OutputData> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(fields);

        csvFileWriter = new FlatFileItemWriterBuilder<OutputData>()
                .name("csvFileWriter")
                .resource(new FileSystemResource(outputFilePath))
                .lineAggregator(new DelimitedLineAggregator<OutputData>() {{
                    setDelimiter(",");
                    setFieldExtractor(fieldExtractor);
                }})
                .headerCallback(writer -> writer.write(String.join(",", fields)))
                .build();

        csvFileWriter.setShouldDeleteIfExists(true);
        csvFileWriter.setShouldDeleteIfEmpty(true);
        csvFileWriter.open(new ExecutionContext());
    }

}
