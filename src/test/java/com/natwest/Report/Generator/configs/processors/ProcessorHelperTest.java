package com.natwest.Report.Generator.configs.processors;

import com.natwest.Report.Generator.entities.InputData;
import com.natwest.Report.Generator.entities.OutputData;
import com.natwest.Report.Generator.entities.ReferenceData;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.powermock.reflect.Whitebox;
import org.springframework.util.ResourceUtils;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProcessorHelperTest {

    @Mock
    private Logger mockLogger;

    @Mock
    private FileReader mockFileReader;

    @Mock
    private CSVReader mockCSVReader;

    @Spy
    @InjectMocks
    private ProcessorHelper processorHelper;

    @BeforeEach
    public void initMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOutputDataPostComputation(){
        InputData inputData = new InputData("field1", "field2", "2.0", "field4", 5.0, "refKey1","refKey2");
        ReferenceData referenceData = new ReferenceData("refKey1","refData1","refKey2","refData2","refData3",10.0);

        OutputData actualOutput = processorHelper.getOutputDataPostComputation(inputData,referenceData);
        OutputData expectedOutput = new OutputData("field1field2", "refData1", "refData2refData3", "20.0", "10.0");

        assertEquals(expectedOutput,actualOutput);
    }

    @Test
    public void testGetOutputData_Success() throws Exception {
        InputData inputData = new InputData("field1", "field2", "2.0", "field4", 5.0, "refKey1","refKey2");

        ReferenceData referenceData = new ReferenceData("refKey1","refData1","refKey2","refData2","refData3",10.0);
        Map<String, ReferenceData> referenceDataMap = Collections.singletonMap("refKey1-refKey2", referenceData);

        doReturn(referenceDataMap).when(processorHelper).getMapFromCSVFile(anyString());

        OutputData actualOutput = processorHelper.getOutputData(inputData, "reference.csv");
        OutputData expectedOutput = new OutputData("field1field2", "refData1", "refData2refData3", "20.0", "10.0");

        assertEquals(expectedOutput,actualOutput);
    }

    @Test
    public void testGetOutputData_NoReferenceData() throws Exception {
        InputData inputData = new InputData("field1", "field2", "2.0", "field4", 5.0, "refKey1","refKey2");

        Map<String, ReferenceData> referenceDataMap = new HashMap<>();

        doReturn(referenceDataMap).when(processorHelper).getMapFromCSVFile(anyString());

        Exception exception = assertThrows(Exception.class, () -> {
            processorHelper.getOutputData(inputData, "reference.csv");
        });

        assertEquals("No Related Reference Data Found", exception.getMessage());
    }



}
