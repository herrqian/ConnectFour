FROM adoptopenjdk/openjdk14:alpine-slim
FROM hseeberger/scala-sbt:8u212_1.2.8_2.13.0
EXPOSE 11111
WORKDIR /connectfour
ADD /target/scala-2.12/ModelModule-assembly-0.1.0-SNAPSHOT.jar /connectfour
ENV DOCKERENV="TRUE"
ENTRYPOINT ["java", "-jar", "ModelModule-assembly-0.1.0-SNAPSHOT.jar"]
# Build it.
RUN sbt compile
CMD sbt run