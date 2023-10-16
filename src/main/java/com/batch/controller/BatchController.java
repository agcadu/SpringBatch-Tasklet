package com.batch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> receiveFile(@RequestParam(name = "file") MultipartFile multipartFile) {

        String fileName = multipartFile.getOriginalFilename();

        try {
            // Construimos la ruta donde se guardará el nuevo archivo fuera del JAR
            Path externalPath = Paths.get("/files/" + fileName);

            // Copiamos el archivo recibido a la ubicación externa
            Files.createDirectories(externalPath.getParent());
            Files.copy(multipartFile.getInputStream(), externalPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("---->Iniciando proceso Batch");

            // Creamos los parámetros para el trabajo Batch, incluyendo la fecha actual y la ruta externa del archivo
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("date", new Date())
                    .addString("filePath", externalPath.toString())
                    .toJobParameters();

            // Ejecutamos el trabajo Batch con los parámetros
            jobLauncher.run(job, jobParameters);

            // Preparamos la respuesta con información sobre el archivo recibido
            Map<String, String> response = new HashMap<>();
            response.put("archivo", fileName);
            response.put("estado", "recibido");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al iniciar el proceso Batch: " + e.getMessage());
            throw new RuntimeException("Error al iniciar el proceso Batch", e);
        }
    }
}
