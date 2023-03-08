FROM openjdk:17-ea-jdk-oracle
WORKDIR /app
COPY target/*.jar app.jar
CMD ["java", "-jar", "/app/app.jar"]