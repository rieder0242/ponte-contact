# Use an official Maven image as the base image
FROM maven:3.8.1-openjdk-17-slim AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
#RUN mvn clean package -DskipTests
RUN mvn clean compile assembly:single -DskipTests
# Use an official OpenJDK image as the base image
#FROM openjdk:11-jre-slim
FROM openjdk:17.0.1-slim
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY target/ponte-contact-1.0-SNAPSHOT-jar-with-dependencies.jar ./
# Set the command to run the application
CMD ["java", "-jar", "ponte-contact-1.0-SNAPSHOT-jar-with-dependencies.jar"]
