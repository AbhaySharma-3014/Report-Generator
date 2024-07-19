package com.natwest.Report.Generator.configs;

import com.natwest.Report.Generator.entities.InputData;
import com.natwest.Report.Generator.entities.OutputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobNotificationService listener;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ItemReader<InputData> reader;

    @Autowired
    private ItemProcessor<InputData, OutputData> processor;

    @Autowired
    private ItemWriter<OutputData> writer;

    @Autowired
    private ConfigHelper configHelper;

    @Bean
    public Job reportGenerationJob() {
        LOGGER.info("Inside reportGenerationJob");
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(reportGenerationSteps())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step reportGenerationSteps() {
        LOGGER.info("Inside reportGenerationSteps");

        String chunkSize = configHelper.getChunkSize();

        return new StepBuilder("jobStep", jobRepository)
                .<InputData,OutputData>chunk(Integer.parseInt(chunkSize), transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

    }
}