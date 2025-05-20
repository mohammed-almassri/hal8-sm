FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/chat-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=0 /app/dependencies/ ./dependencies/
COPY --from=0 /app/spring-boot-loader/ ./spring-boot-loader/
COPY --from=0 /app/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=0 /app/application/ ./application/

ENTRYPOINT ["java", "-cp", "dependencies/:spring-boot-loader/:snapshot-dependencies/:application/", "org.springframework.boot.loader.JarLauncher"]
