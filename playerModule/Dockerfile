FROM adoptopenjdk/openjdk14:alpine-slim
FROM hseeberger/scala-sbt:8u212_1.2.8_2.13.0
EXPOSE 22222
WORKDIR /connectfour
ADD /target/scala-2.12/PlayerModule-assembly-0.1.0-SNAPSHOT.jar /connectfour
ENV DOCKERENV="TRUE"
ENTRYPOINT ["java", "-jar", "PlayerModule-assembly-0.1.0-SNAPSHOT.jar"]
#CMD java -cp "${CLASSPATH}:${SCALA_HOME}/lib/scala-library.jar:target/scala-2.12/ControlModule-assembly-0.1.0-SNAPSHOT.jar" ConnectFour

# Build it.
RUN sbt compile
CMD sbt run
