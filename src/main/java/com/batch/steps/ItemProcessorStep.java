package com.batch.steps;

import com.batch.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemProcessorStep implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("---->Iniciando procesado de datos");

        //Recuperamos la lista de personas del contexto
        List<Person> personList = (List<Person>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("personList");

        //Procesamos la lista de personas y añadimos la fecha de inserción
        List<Person> personFinalList = personList.stream().map(person ->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            person.setInsertionDate(formatter.format(LocalDateTime.now()));
            return person;
        }).collect(Collectors.toList());

        //Guardamos la lista de personas en el contexto para poder recuperarla en el siguiente paso sobreescribiendo la lista anterior
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("personList", personFinalList);

        log.info("---->Finalizado procesado de datos");

        return RepeatStatus.FINISHED;
    }
}
