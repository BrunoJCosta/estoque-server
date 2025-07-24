FROM amazoncorretto:21

COPY target/estoque-0.0.1-SNAPSHOT.jar /app/estoque-0.0.1-SNAPSHOT.jar

EXPOSE 8200

CMD ["java", "-jar", "/app/estoque-0.0.1-SNAPSHOT.jar"]