FROM maven:3.8.4-openjdk-17-slim as builder

WORKDIR /app
COPY . .

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=builder /app/target/iwf-calculator.jar /app/iwf-calculator.jar

EXPOSE 8080

CMD ["java", "-jar", "app/iwf-calculator.jar"]
