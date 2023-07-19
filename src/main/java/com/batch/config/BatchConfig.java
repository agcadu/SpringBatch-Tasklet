package com.batch.config;

import com.batch.steps.ItemDescompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWritterStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @JobScope
    public ItemDescompressStep itemDescompressStep() {
        return new ItemDescompressStep();
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep() {
        return new ItemReaderStep();
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep() {
        return new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemWritterStep itemWritterStep() {
        return new ItemWritterStep();
    }

    //Creamos los steps que se ejecutarán en el job
    @Bean
    public Step descompressStep() {
        return stepBuilderFactory.get("itemDescompressStep")
                .tasklet(itemDescompressStep())
                .build();
    }

    @Bean
    public Step readerStep() {
        return stepBuilderFactory.get("itemReaderStep")
                .tasklet(itemReaderStep())
                .build();
    }

    @Bean
    public Step processorStep() {
        return stepBuilderFactory.get("itemProcessorStep")
                .tasklet(itemProcessorStep())
                .build();
    }

    @Bean
    public Step writterStep() {
        return stepBuilderFactory.get("itemWritterStep")
                .tasklet(itemWritterStep())
                .build();
    }

    //Creamos el job con los steps
    @Bean
    public Job readCSVJob() {
        return jobBuilderFactory.get("readCSVJob")
                .start(descompressStep())
                .next(readerStep())
                .next(processorStep())
                .next(writterStep())
                .build();
    }


}


