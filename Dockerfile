FROM openjdk:17-alpine
WORKDIR /app
COPY target/Country-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5051
CMD ["java","-jar","app.jar"]