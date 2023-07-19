package com.batch.steps;

import com.batch.entities.Person;
import com.batch.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ItemWritterStep implements Tasklet {

    @Autowired
    private IPersonService personService;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("---->Iniciando escritura de datos");

        List<Person> personList = (List<Person>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("personList");

        personList.forEach(person ->{
            if(person != null){
                log.info(person.toString());
            }
        });

        personService.saveAll(personList);

        log.info("---->Finalizada escritura de datos");

        return RepeatStatus.FINISHED;
    }
}
