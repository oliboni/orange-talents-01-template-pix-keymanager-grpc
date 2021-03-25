FROM openjdk:11
#ARG JAR_FILE=build/libs/*.jar
COPY build/libs/*all.jar app.jar
EXPOSE 50051
ENTRYPOINT ["java", "-jar", "/app.jar"]