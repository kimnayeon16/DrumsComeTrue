FROM openjdk:11 as builder
ENV APP_HOMES = /app
# WORKDIR $APP_HOMES
COPY build.gradle settings.gradle gradlew $APP_HOMES
COPY gradle $APP_HOMES/gradle
COPY src $APP_HOMES/src

RUN chmod +x $APP_HOMES/gradlew
RUN $APP_HOMES/gradlew clean build
RUN $APP_HOMES/gradlew build
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
