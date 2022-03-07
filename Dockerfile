FROM openjdk:17-jdk-slim
RUN adduser --system --group wagner
USER wagner:wagner
ARG JAR_FILE=build/libs/*.war
COPY ${JAR_FILE} app.war
ENTRYPOINT ["java","-jar","/app.war"]