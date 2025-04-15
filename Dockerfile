FROM adoptopenjdk/maven-openjdk11:latest
EXPOSE 8080
ARG JAR_FILE="target/zf-mp-shutterstock-connector-0.0.6.jar"
ADD ${JAR_FILE} zf-mp-shutterstock-connector.jar
ENTRYPOINT [ "java", "-jar", "/zf-mp-shutterstock-connector.jar" ]