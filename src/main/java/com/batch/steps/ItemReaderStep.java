package com.batch.steps;

import com.batch.entities.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemReaderStep implements Tasklet {

    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("---->Iniciando lectura de datos");

        //Leemos el fichero de personas
        Reader reader = new FileReader(resourceLoader.getResource("classpath:files/destination/persons.csv").getFile());

        CSVParser parser = new CSVParserBuilder().withSeparator(',').build();

        CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();

        //Guardamos los datos en una lista de personas para poder procesarlos
        List<Person> personList = new ArrayList<>();
        String[] line;

        while ((line = csvReader.readNext()) != null) {
            Person person = new Person();
            person.setName(line[0]);
            person.setLastName(line[1]);
            person.setAge(Integer.parseInt(line[2]));

            personList.add(person);
        }

        csvReader.close();

        log.info("---->Finalizada lectura de datos");

        //Guardamos la lista de personas en el contexto para poder recuperarla en el siguiente paso
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("personList", personList);


        return RepeatStatus.FINISHED;
    }
}
