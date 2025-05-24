# Product price comparator
## Overview 
This is a Spring Boot backend application developed for Accesa's coding challenge. It is build for price comparison and it allows users to:
- List the best discounts available
- List new discounts
- Optimize shopping lists
- Obtain historical data for product prices
- Finding the best buys regardless of package size
- Set custom price alerts

### Project Structure
- 'controller/': REST endpoints
- 'service/': business logic
- 'repository/': JPA interfaces for db access
- 'dto/': Data transfer objects for request/response
- 'entity/': Database Models
- 'mapper/': Converters between entities and Dtos

## Build and run Instructions

### Prequisites
- Java 21+
- Maven 4.0
- PostgreSQL

### Running the app
```bash
# Clone the repo
git clone https://github.com/arcademiss/product-comparator.git
cd product-comparator

# Build with maven
mvn clean install

# Run the application
mvn spring-boot:run
