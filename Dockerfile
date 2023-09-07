FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /zoo-lab
COPY . /zoo-lab/.
RUN mvn -f /zoo-lab/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /zoo-lab
COPY --from=builder /zoo-lab/target/*.jar /zoo-lab/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/zoo-lab/*.jar"]