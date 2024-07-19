package com.natwest.Report.Generator.configs;

import com.natwest.Report.Generator.configs.readers.CsvFileReader;
import com.natwest.Report.Generator.configs.writers.CsvFileWriter;
import com.natwest.Report.Generator.entities.InputData;
import com.natwest.Report.Generator.entities.OutputData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommonConfigTest {

    @Mock
    private ConfigHelper mockConfigHelper;

    @Mock
    private Logger mockLogger;

    @Spy
    @InjectMocks
    private CommonConfig commonConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReader_CsvFile() throws Exception {

        String inputFilePath = "test.csv";
        when(mockConfigHelper.getInputFilePath()).thenReturn(inputFilePath);

        CsvFileReader mockCsvFileReader = mock(CsvFileReader.class);
        doReturn(mockCsvFileReader).when(commonConfig).getCsvFileReader(inputFilePath);

        ItemReader<InputData> reader = commonConfig.reader(inputFilePath);

        assertEquals(mockCsvFileReader, reader);
    }

    @Test
    public void testReader_JsonFile() throws Exception {

        String inputFilePath = "test.json";
        when(mockConfigHelper.getInputFilePath()).thenReturn(inputFilePath);

        CsvFileReader mockCsvFileReader = mock(CsvFileReader.class);
        doReturn(mockCsvFileReader).when(commonConfig).getCsvFileReader(inputFilePath);

        Exception exception = assertThrows(Exception.class, () -> {
            commonConfig.reader(inputFilePath);
        });

        assertEquals("Invalid Input File extension found", exception.getMessage());
    }

    @Test
    public void testReader_NoExtensionFile() throws Exception {

        String inputFilePath = "test";
        when(mockConfigHelper.getInputFilePath()).thenReturn(inputFilePath);

        CsvFileReader mockCsvFileReader = mock(CsvFileReader.class);
        doReturn(mockCsvFileReader).when(commonConfig).getCsvFileReader(inputFilePath);

        Exception exception = assertThrows(Exception.class, () -> {
            commonConfig.reader(inputFilePath);
        });

        assertEquals("No file extension found", exception.getMessage());
    }

    @Test
    public void testItemWriter_CsvFile() throws Exception {

        String outputFilePath = "output.csv";
        when(mockConfigHelper.getOutputFilePath()).thenReturn(outputFilePath);

        CsvFileWriter mockCsvFileWriter = mock(CsvFileWriter.class);
        doReturn(mockCsvFileWriter).when(commonConfig).getCsvFileWriter(outputFilePath);
        ItemWriter<OutputData> writer = commonConfig.itemWriter(outputFilePath);

        assertEquals(mockCsvFileWriter, writer);
    }

    @Test
    public void testItemWriter_JsonFile() throws Exception {

        String outputFilePath = "output.json";
        when(mockConfigHelper.getOutputFilePath()).thenReturn(outputFilePath);

        CsvFileWriter mockCsvFileWriter = mock(CsvFileWriter.class);
        doReturn(mockCsvFileWriter).when(commonConfig).getCsvFileWriter(outputFilePath);

        Exception exception = assertThrows(Exception.class, () -> {
            commonConfig.itemWriter(outputFilePath);
        });

        assertEquals("Invalid Output File extension found", exception.getMessage());
    }


    @Test
    public void testItemWriter_NoExtensionFile() throws Exception {

        String outputFilePath = "output";
        when(mockConfigHelper.getOutputFilePath()).thenReturn(outputFilePath);

        CsvFileWriter mockCsvFileWriter = mock(CsvFileWriter.class);
        doReturn(mockCsvFileWriter).when(commonConfig).getCsvFileWriter(outputFilePath);

        Exception exception = assertThrows(Exception.class, () -> {
            commonConfig.itemWriter(outputFilePath);
        });

        assertEquals("No file extension found", exception.getMessage());
    }
}
