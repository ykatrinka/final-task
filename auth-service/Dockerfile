FROM openjdk:21-slim
COPY auth-service-api/build/libs/*.jar auth-service.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=demo","auth-service.jar"]

#EXPOSE 8003
