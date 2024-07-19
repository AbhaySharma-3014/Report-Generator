package com.natwest.Report.Generator.configs.writers;

import com.natwest.Report.Generator.entities.OutputData;
import org.springframework.batch.item.ItemWriter;

public interface FileWriter extends ItemWriter<OutputData> {
    public void buildWriter(String outputFilePath);
}
