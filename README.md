# Currency Converter Microservices

A microservices-based currency conversion application that demonstrates fundamental microservices concepts and best practices.

## Architecture Overview

This project consists of four microservices:

1. **Currency Exchange Service**: Retrieves real-time exchange rates from an external API
2. **Currency Conversion Service**: Performs currency conversions using exchange rates
3. **Naming Server**: Eureka service registry for service discovery
4. **API Gateway**: Routes requests to appropriate microservices

## Technologies Used

- **Spring Boot**: Framework for creating microservices
- **Spring Cloud**: For microservices architecture patterns
- **Netflix Eureka**: Service discovery
- **OpenFeign**: Declarative REST client
- **Resilience4j**: Circuit breaker pattern implementation
- **Docker**: Containerization
- **JUnit & Mockito**: Testing frameworks

## Features

- Real-time currency conversion between multiple currencies
- Fault tolerance with circuit breakers and fallbacks
- Service discovery
- Centralized routing through API Gateway
- Simple and intuitive user interface
