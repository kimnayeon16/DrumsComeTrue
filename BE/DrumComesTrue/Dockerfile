### multi-stage build ### 

### build stage ###
FROM openjdk:11 as builder
ENV WORKSPACE /spring-docker
WORKDIR $WORKSPACE

# copy build & code
COPY build.gradle settings.gradle gradlew .
COPY gradle gradle
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar
#RUN ./gradlew bootJar

### create image stage ###
FROM openjdk:11

# copy from build stage
ENV WORKSPACE /spring-docker
WORKDIR $WORKSPACE

COPY --from=builder $WORKSPACE/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
