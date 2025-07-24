FROM amazoncorretto:21

COPY target/estoque-server-0.0.1-SNAPSHOT.jar /app/estoque-v1.jar

EXPOSE 8200

CMD ["java", "-jar", "/app/estoque-v1.jar"]