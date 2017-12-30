FROM openjdk:8-jdk-alpine
MAINTAINER matrus.eu

COPY UnlimitedJCEPolicyJDK8/* ${JAVA_HOME}/jre/lib/security/
COPY target/passmanager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]