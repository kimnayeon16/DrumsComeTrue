FROM openjdk:11 as builder
ENV APP_HOME /app
#WORKDIR $APP_HOMES
COPY build.gradle settings.gradle gradlew .
COPY gradle gradle
COPY src src

RUN chmod +x ./gradlew
#RUN ./gradlew clean bootJar
RUN ./gradlew bootJar
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
