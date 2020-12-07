FROM adoptopenjdk/openjdk11:jre-11.0.9_11-alpine
COPY target/socket-*-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
