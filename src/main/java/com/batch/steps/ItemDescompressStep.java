package com.batch.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class ItemDescompressStep implements Tasklet {

    @Autowired
    private ResourceLoader resourceLoader;



    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("----> Iniciando descompresión de datos");

        String externalFilePath = "/files/persons.zip"; // Ruta del archivo zip que se encuentra fuera del JAR

        // Recuperamos el fichero zip del externalFilePath y lo descomprimimos en la carpeta destination del mismo directorio donde se encuentra el zip
        Resource resource = resourceLoader.getResource("file:" + externalFilePath);

        try (InputStream is = resource.getInputStream();
             ZipInputStream zis = new ZipInputStream(is)) {

            File destDir = new File("/files/destination");

            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {
                File destFile = new File(destDir, zipEntry.getName());

                if (zipEntry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    File parentDir = destFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    try (FileOutputStream fos = new FileOutputStream(destFile)) {
                        byte[] buffer = new byte[1024];
                        int length;

                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }

                zis.closeEntry();
            }
        }

        log.info("----> Finalizada descompresión de datos");

        return RepeatStatus.FINISHED;
    }
}

