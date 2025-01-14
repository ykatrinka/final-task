#FROM gradle:8.11-jdk21 AS build
#COPY --chown=gradle:gradle . /app
#WORKDIR /app
#RUN gradle build -x test -Dspring.profiles.active=demo


FROM openjdk:21-slim
#COPY --from=build app/news-service-api/build/libs/*.jar news-service.jar
COPY news-service-api/build/libs/*.jar news-service.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=demo","news-service.jar"]

#EXPOSE 8081

