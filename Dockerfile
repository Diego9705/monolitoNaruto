FROM maven:3.8.5-openjdk-17 as build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN ls -la /app/target

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/monolitoNaruto-0.0.1-SNAPSHOT.jar /app/monolitoNaruto.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/monolitoNaruto.jar"]