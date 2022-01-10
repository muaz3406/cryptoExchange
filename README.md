# Spring Boot Crypto converter


Context:

  - [**Getting Started**](#getting-started)
  - [**Spring Rest Service**](#spring-rest-service)
  - [**Build & Run Application**](#build-run-application)
  - [**Endpoints with Swagger**](#endpoints-with-swagger)
  - [**Endpoints with Postman**](#endpoints-with-postman)

## Getting Started

In this project, I used Redis for caching and PostgreSql with Spring Boot.
USD and EUR convert to BTC and request info caches in redis.
Calculated result expires after 10 sec.
Rate info expires after 15min.

## Spring Rest Service

Getting btc price for 1 unit
```
http://localhost:8080/getRate/USD

```

createExchangeOffer
```
http://localhost:8080/createExchangeOffer

{
    "fromCurrency":"USD",
    "customerId":1,
    "value":"100"
}
```

buyExchangeOffer
```
http://localhost:8080/buyExchangeOffer/1

cached offer will be bought by customer id
```

## Build & Run Application

* Build Java Jar. main test class has a problem but others succeed

```shell
mvn clean install -DskipTests
```

*  Docker Compose Build and Run

```shell
docker-compose build --no-cache
docker-compose up --force-recreate

```

After running the application you can visit `http://localhost:8080`.	

## Endpoints with Swagger


You can see the endpoint in `http://localhost:8080/swagger-ui.html` page.
I used Swagger for visualization endpoints.

## Endpoints with Postman
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/b1232f959768f2ca48dc)




