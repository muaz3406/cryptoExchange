FROM openjdk:11
EXPOSE 8080
ADD target/cryptoExchange-0.0.1-SNAPSHOT.jar cryptoExchange-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","cryptoExchange-0.0.1-SNAPSHOT.jar"]