FROM openjdk:17-jdk-alpine

RUN mkdir /files

COPY target/SpringBatch-0.0.1-SNAPSHOT.jar SpringBatch_Tasklet.jar

RUN chmod -R 777 /files

WORKDIR /

ENTRYPOINT ["java", "-jar", "/SpringBatch_Tasklet.jar"]




