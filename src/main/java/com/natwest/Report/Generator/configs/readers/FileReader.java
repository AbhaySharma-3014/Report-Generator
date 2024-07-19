package com.natwest.Report.Generator.configs.readers;

import com.natwest.Report.Generator.entities.InputData;
import org.springframework.batch.item.ItemReader;

public interface FileReader extends ItemReader<InputData> {
    public void buildReader(String inputFilePath);
}
