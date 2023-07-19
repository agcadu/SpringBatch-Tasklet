package com.batch.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class ItemDescompressStep implements Tasklet {

    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("---->Iniciando descompresión de datos");

        Resource resource = resourceLoader.getResource("classpath:files/person.zip");
        String filePath = resource.getFile().getAbsolutePath();

        ZipFile zipFile = new ZipFile(filePath);
        File destDir = new File(resource.getFile().getParent(), "destination");

        if (!destDir.exists()) {
            destDir.mkdir();
        }

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            File destFile = new File(destDir, zipEntry.getName());
            if(zipEntry.isDirectory()){
                destFile.mkdir();
        }else{
                InputStream is = zipFile.getInputStream(zipEntry);
                FileOutputStream fos = new FileOutputStream(destFile);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }

                is.close();
                fos.close();

            }
        }

        zipFile.close();

        log.info("---->Finalizada descompresión de datos");

        return RepeatStatus.FINISHED;
    }
}
