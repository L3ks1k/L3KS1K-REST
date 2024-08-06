FROM jelastic/maven:3.9.4-openjdk-22.ea-b17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar
COPY --from=build /app/target/classes/key-pkcs8.der /app/key-pkcs8.der
COPY --from=build /app/target/classes/public.der /app/public.der
COPY --from=build /app/target/classes/weakpass.txt /app/weakpass.txt
ENTRYPOINT ["java","-jar","/app/app.jar"]



