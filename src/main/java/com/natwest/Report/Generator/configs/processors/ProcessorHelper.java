package com.natwest.Report.Generator.configs.processors;

import com.natwest.Report.Generator.entities.InputData;
import com.natwest.Report.Generator.entities.OutputData;
import com.natwest.Report.Generator.entities.ReferenceData;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ProcessorHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorHelper.class);

    public OutputData getOutputData(InputData item, String referenceFilePath) throws Exception {
        Map<String, ReferenceData> referenceDataMap = getMapFromCSVFile(referenceFilePath);
        ReferenceData referenceData = referenceDataMap.get(item.getRefKey1() + "-" + item.getRefKey2());

        if(Objects.nonNull(referenceData)) {
            return getOutputDataPostComputation(item,referenceData);
        }else{
            LOGGER.error("EXCEPTION OCCURRED - No Related Reference Data Found for refKey1: {} and refKey2: {} ", item.getRefKey1() ,item.getRefKey2());
            throw new Exception("No Related Reference Data Found");
        }
    }

    public OutputData getOutputDataPostComputation(InputData inputData, ReferenceData referenceData) {
        String outfield1 = inputData.getField1() + inputData.getField2();
        String outfield2 = referenceData.getRefdata1();
        String outfield3 = referenceData.getRefdata2() + referenceData.getRefdata3();
        String outfield4 = String.valueOf(Double.parseDouble(inputData.getField3()) * Math.max(inputData.getField5(), referenceData.getRefdata4()));
        String outfield5 = String.valueOf(Math.max(inputData.getField5(), referenceData.getRefdata4()));
        return new OutputData(outfield1, outfield2, outfield3, outfield4, outfield5);
    }

    public Map<String, ReferenceData> getMapFromCSVFile(String csvFilePath) throws Exception {
        Map<String, ReferenceData> referenceDataMap = new HashMap<>();

        FileReader fileReader = new FileReader(ResourceUtils.getFile(csvFilePath));
        CSVReader csvReader = new CSVReader(fileReader);
        csvReader.skip(1);
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            ReferenceData referenceData = getReferenceDataFromCSVdataRow(nextRecord);
            referenceDataMap.put(referenceData.getRefkey1() + "-" + referenceData.getRefkey2(), referenceData);
        }
        csvReader.close();
        fileReader.close();
        return referenceDataMap;
    }

    private ReferenceData getReferenceDataFromCSVdataRow(String[] csvDataRow) throws Exception {
        Field[] fields = ReferenceData.class.getDeclaredFields();
        int numOfParametersInReferenceClass = fields.length;
        if (Objects.equals(numOfParametersInReferenceClass,csvDataRow.length)) {
            ReferenceData referenceData = new ReferenceData();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                if (String.class.equals(field.getType())) {
                    field.set(referenceData, csvDataRow[i]);
                } else if (Double.class.equals(field.getType()) || (double.class.equals(field.getType()))) {
                    field.set(referenceData, Double.valueOf(csvDataRow[i]));
                }
            }
            return referenceData;
        }else{
            LOGGER.error("EXCEPTION OCCURRED - Number of Parameters in CSV Row and Entity Model is not same");
            throw new Exception("Number of Parameters in CSV Row and Entity Model is not same");
        }
    }

}


