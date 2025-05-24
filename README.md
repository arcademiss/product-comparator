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

---

```

## Assumptions made or simplifications

- Discounts are assumed to apply per store and within a specific date range
- No authenticatio/authorization is needed.
- Because of Google's new TOS for STMP, email alerts are just printed to the console and not actually sent
- The behaviour of LocalDate.now() is replaced by custom dates in the request to facilitate testing
- Security was not the main focus of this challenge
- Implementation tests are not needed

## API Usage

Base URL: `https://localhost:8080/api`

### 1. Get all discounts
**GET** `/api/discounts?startDate=&endDate=`

#### Description:
Fetches all discounts in the given range.

#### Curl example:
```bash
curl -X 'GET' \
  'http://localhost:8080/api/discounts?startDate=2025-05-02&endDate=2025-05-02' \
  -H 'accept: */*'
```

#### Response example:
```json
[
  {
    "id": 334,
    "productName": "detergent lichid",
    "brand": "Ariel",
    "store": "kaufland",
    "packageQuantity": 2.5,
    "packageUnit": "l",
    "fromDate": "2025-05-01",
    "toDate": "2025-05-07",
    "percentage": 22
  },
  {
    "id": 391,
    "productName": "detergent lichid",
    "brand": "Dero",
    "store": "profi",
    "packageQuantity": 2.5,
    "packageUnit": "l",
    "fromDate": "2025-05-01",
    "toDate": "2025-05-07",
    "percentage": 20
  }
  ]
```


  
