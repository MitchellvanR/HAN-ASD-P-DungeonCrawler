FROM maven:3.9.5 AS JAR_BUILDER
COPY ./ ./
RUN mvn clean package


FROM openjdk:21
COPY --from=JAR_BUILDER app/target/app-1-jar-with-dependencies.jar app.jar
ENTRYPOINT java -jar app.jar
