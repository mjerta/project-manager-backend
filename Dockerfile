# Use an official Maven image to build the application
FROM maven:3.9.8-amazoncorretto-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official Amazon Corretto runtime as a parent image
FROM amazoncorretto:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/project-manager-backend-0.0.1-SNAPSHOT.jar app.jar

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

