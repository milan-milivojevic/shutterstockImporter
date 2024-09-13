FROM adoptopenjdk/maven-openjdk11:latest
EXPOSE 18080
ARG JAR_FILE="target/zf-mp-shutterstock-connector-0.0.1.jar"
ADD ${JAR_FILE} mp-shutterstock-connector.jar
ENTRYPOINT [ "java", "-jar", "/zf-mp-shutterstock-connector.jar" ]