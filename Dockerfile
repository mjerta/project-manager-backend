# Use an official Maven image to build the application
FROM maven:3.9.8-amazoncorretto-21 AS build

# Force TLS 1.2 during Maven downloads to avoid handshake issues with older proxies
ENV MAVEN_OPTS="-Djdk.tls.client.protocols=TLSv1.2 -Dhttps.protocols=TLSv1.2"

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper (if present) and pom.xml first to leverage Docker cache
COPY .mvn .mvn
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official Amazon Corretto runtime as a parent image
FROM amazoncorretto:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
