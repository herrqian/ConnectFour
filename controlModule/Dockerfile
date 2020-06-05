FROM adoptopenjdk/openjdk14:alpine-slim
FROM hseeberger/scala-sbt:8u212_1.2.8_2.13.0
EXPOSE 8080
WORKDIR /connectfour
ADD /target/scala-2.12/ControlModule-assembly-0.1.0-SNAPSHOT.jar /connectfour
ENV DOCKERENV="TRUE"
ENTRYPOINT ["java", "-jar", "ControlModule-assembly-0.1.0-SNAPSHOT.jar"]
# Build it.
RUN sbt compile
CMD sbt run