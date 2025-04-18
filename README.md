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
- Centralised routing through API Gateway
- Simple and intuitive user interface

Services
Naming Server (Eureka)

Port: 8761
Handles service registration and service discovery
Allows services to find and communicate with each other without hardcoded URLS

Currency Exchange Service

Port: 8000
Provides exchange rates between different currencies
Fetches real-time exchange rates from an external API
Implements proper error handling and resilience

Currency Conversion Service

Port: 8100
Calculates converted amounts based on quantity and exchange rates
Uses Feign client to communicate with Currency Exchange Service
Implements circuit breaker and retry patterns for resilience

API Gateway

Port: 8765
Acts as a single entry point for the frontend
Routes requests to the appropriate microservices
Provides cross-cutting concerns like logging
