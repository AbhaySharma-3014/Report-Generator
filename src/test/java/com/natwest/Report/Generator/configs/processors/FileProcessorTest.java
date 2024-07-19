package com.natwest.Report.Generator.configs.processors;

import com.natwest.Report.Generator.entities.InputData;
import com.natwest.Report.Generator.entities.OutputData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FileProcessorTest {

    @Mock
    private ProcessorHelper processorHelper;

    @InjectMocks
    private FileProcessor fileProcessor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessValidInputData() throws Exception {
        InputData inputData = new InputData("field1", "field2", "1.0", "field4", 5.0, "refKey1","refKey2");
        OutputData expectedOutput = new OutputData("field1field2", "refdata1", "refdata2refdata3", "25.0", "5.0");

        doReturn(expectedOutput).when(processorHelper).getOutputData(any(),any());

        OutputData actualOutput = fileProcessor.process(inputData);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcessNullInputData() throws Exception {
        InputData inputData = null;

        Exception exception = assertThrows(Exception.class, () -> {
            fileProcessor.process(inputData);
        });

        assertEquals("No Input Data Found", exception.getMessage());
    }
}
