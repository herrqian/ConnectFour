FROM adoptopenjdk/openjdk14:slim
FROM hseeberger/scala-sbt:8u212_1.2.8_2.13.0
WORKDIR /connectfour
ADD /target/scala-2.12/connect-four-assembly-0.1.0-SNAPSHOT.jar /connectfour
ENV DOCKERENV="TRUE"
CMD java -jar connect-four-assembly-0.1.0-SNAPSHOT.jar
CMD sbt run

