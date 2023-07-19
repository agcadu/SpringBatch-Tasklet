package com.batch.config;

import com.batch.steps.ItemDescompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWritterStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

    @Bean
    public ItemDescompressStep itemDescompressStep() {
        return new ItemDescompressStep();
    }

    @Bean
    public ItemReaderStep itemReaderStep() {
        return new ItemReaderStep();
    }

    @Bean
    public ItemProcessorStep itemProcessorStep() {
        return new ItemProcessorStep();
    }

    @Bean
    public ItemWritterStep itemWritterStep() {
        return new ItemWritterStep();
    }


}


