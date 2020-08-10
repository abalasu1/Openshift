FROM ppc64le/openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM registry.access.redhat.com/ubi8/ubi:8.2

RUN dnf install -y java-11-openjdk.ppc64le

COPY --from=build /workspace/app/target/basic-op-add-1.0.0.jar /app.jar

EXPOSE 8001/tcp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

#CMD ["java", "-jar", "./server.jar"]
#FROM openjdk:8-jdk-alpine
#COPY --from=build /workspace/app/target/basic-op-add-1.0.0.jar /app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
