# Country Microservice
Microservice built with SpringBoot, Java 17, Maven, MongoDB, and Apache Kafka.  
---
## Technologies
- Java 17  
- SpringBoot  
- Maven  
- MongoDB  
- Apache Kafka    
- Docker  
---
## Endpoints

### GET /capital/{capital-name} 
Returns country information using RestCountries API.

**Sample Response**
```json
{
  "countryName": "Greece",
  "countryCode": "GR",
  "capital": "Athens",
  "continent": "Europe",
  "officialLanguage": "Greek (modern)",
  "currencyName": "Euro"
}
```

### GET /country?country-name={country-name}
-Returns country information using RestCountries API. 
-A caching mechanism is implemented for the flows using MongoDB.
-TTL index: 10 minutes.
-If a request is repeated within TTL, the response is returned from cache instead of RestCountries API.

### POST /kafka (Kafka Integration)
Produces a country object to a Kafka topic.

Request Body:
```json
{
  "countryName": "Greece",
  "countryCode": "GR",
  "capital": "Athens",
  "continent": "Europe",
  "officialLanguage": "Greek (modern)",
  "currencyName": "Euro"
}
```
### GET /kafka/{country-name}
Returns a list of stored country objects from MongoDB collection KafkaCountries.

Sample Response
```json
[
  {
    "countryName": "Greece",
    "countryCode": "GR",
    "capital": "Athens",
    "continent": "Europe",
    "officialLanguage": "Greek (modern)",
    "currencyName": "Euro"
  }
]
```
### Additional Features
-Unit tests with JUnit & Mockito
-Docker image creation and container deployment
-Fully asynchronous communication with external APIs and Kafka

### Project Summary
A reactive, fully asynchronous microservice integrating external APIs, caching, distributed streaming, and non-blocking communication.
Designed for demonstrating modern Java microservice practices.

### Architecture
-Microservice → Exposes REST endpoints
-MongoDB → Caching & storage of Kafka messages
-Kafka → Distributed streaming between producer and consumer
-External API → RestCountries API for country info
