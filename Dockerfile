FROM eclipse-temurin:21-jre-alpine

WORKDIR ./app

COPY ./target/techWeek25-0.0.1-SNAPSHOT.jar ./BizVisionV1.jar

ENTRYPOINT ["java", "-jar", "BizVisionV1.jar"]
