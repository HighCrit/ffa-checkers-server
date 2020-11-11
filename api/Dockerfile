FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.6_10-slim
RUN addgroup -S javaserver && adduser -S javaserver -G javaserver
USER javaserver:javaserver
COPY target/api-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
