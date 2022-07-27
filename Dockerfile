FROM openjdk:8-alpine

COPY target/uberjar/creditor.jar /creditor/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/creditor/app.jar"]
