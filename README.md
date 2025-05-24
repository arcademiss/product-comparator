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
#### Parameters:
- startDate: the beggining of the interval YYYY-MM-DD
- endDate: the end of the interval YYYY-MM-DD

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

### 2. New discounts
**GET** `api/new-discounts?date=`
#### Parameters:
- date: parameter used for testing, in a real scenario would be replaced by LocalDate.now(); format is YYYY-MM-DD

#### Curl example:
```bash
curl -X 'GET' \
  'http://localhost:8080/api/new-discounts?date=2025-05-09' \
  -H 'accept: */*'
```

#### Example response:

```json
[
  {
    "storeName": "kaufland",
    "productName": "lapte zuzu",
    "discount": 9,
    "oldPrice": 13,
    "newPrice": 11.83,
    "fromDate": "2025-05-08",
    "toDate": "2025-05-14",
    "quantity": 1,
    "unit": "l",
    "normalizedUnit": "l",
    "normalizedQuantity": 1
  },
  {
    "storeName": "kaufland",
    "productName": "ouă mărimea M",
    "discount": 11,
    "oldPrice": 13.6,
    "newPrice": 12.1,
    "fromDate": "2025-05-08",
    "toDate": "2025-05-14",
    "quantity": 10,
    "unit": "buc",
    "normalizedUnit": "buc",
    "normalizedQuantity": 10
  }
]
```
### 3. Optimize shopping baskets

**POST** `api/optimize-basket`

#### Request:
- items: a list of objects
- startDate: parameter used for testing, in a real scenario should be replaced by LocalDate.now()
```json
  {
  "items": [
    {
      "productName": "lapte",
      "quantity": 3,
      "unit": "l",
      "normalizedUnit": "l",
      "normalizedQuantity": 1
    }
  ],
  "startDate": "2025-05-24"
  }
  ```
#### CURL example:
```bash
curl -X 'POST' \
  'http://localhost:8080/api/optimize-basket' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "items": [
    {
      "productName": "lapte",
      "quantity": 3,
      "unit": "string",
      "normalizedUnit": "string",
      "normalizedQuantity": 0
    },
    {
      "productName": "detergent",
      "quantity":5,
      "unit": "string",
      "normalizedUnit": "string",
      "normalizedQuantity": 0
    },
    {
      "productName": "biscu",
      "quantity": 2,
      "unit": "string",
      "normalizedUnit": "string",
      "normalizedQuantity": 0
    },
    {
      "productName": "iaurt",
      "quantity": 1,
      "unit": "string",
      "normalizedUnit": "string",
      "normalizedQuantity": 0
    },
    {
      "productName": "cafea",
      "quantity": 1,
      "unit": "string",
      "normalizedUnit": "string",
      "normalizedQuantity": 0
    }
  ],
  "startDate": "2025-05-14"
}'
```

#### Example response:
```json
{
  "stores": [
    {
      "storeName": "kaufland",
      "items": [
        {
          "productName": "biscuiți cu unt",
          "quantity": 2,
          "unit": "kg",
          "unitPrice": 6.51,
          "totalPrice": 13.02,
          "normalizedUnit": "kg",
          "normalizedQuantity": 2
        }
      ],
      "subTotal": 13.02,
      "savings": 1.78
    },
    {
      "storeName": "lidl",
      "items": [
        {
          "productName": "lapte zuzu",
          "quantity": 3,
          "unit": "l",
          "unitPrice": 8.62,
          "totalPrice": 25.87,
          "normalizedUnit": "l",
          "normalizedQuantity": 3
        },
        {
          "productName": "detergent lichid",
          "quantity": 5,
          "unit": "l",
          "unitPrice": 39.6,
          "totalPrice": 198,
          "normalizedUnit": "l",
          "normalizedQuantity": 5
        },
        {
          "productName": "cafea măcinată",
          "quantity": 1,
          "unit": "kg",
          "unitPrice": 19.04,
          "totalPrice": 19.04,
          "normalizedUnit": "kg",
          "normalizedQuantity": 1
        }
      ],
      "subTotal": 242.91,
      "savings": 56.39
    },
    {
      "storeName": "profi",
      "items": [
        {
          "productName": "iaurt de băut",
          "quantity": 1,
          "unit": "kg",
          "unitPrice": 4.5,
          "totalPrice": 4.5,
          "normalizedUnit": "kg",
          "normalizedQuantity": 1
        }
      ],
      "subTotal": 4.5,
      "savings": 0.5
    }
  ],
  "totalCost": 260.43,
  "totalSavings": 58.67
}
```

### 4. Price history

**GET** `api/price-history?productName=&store=&category=&brand=`

#### Parameters:
- productName: the name of the product
- store: the name of the store(can be null)
- brand: name of the brand(can be null)
- category: the category(can be null)

#### Curl example:
```bash
curl -X 'GET' \
  'http://localhost:8080/api/price-history?productName=iaurt%20grecesc&store=lidl&category=&brand=' \
  -H 'accept: */*'
```

#### Example response:

```json
[
  {
    "productId": "P002",
    "productName": "iaurt grecesc",
    "productCategory": "lactate",
    "productBrand": "Lidl",
    "productStore": "lidl",
    "date": "2025-05-01",
    "price": 11.5,
    "currency": "RON"
  },
  {
    "productId": "P002",
    "productName": "iaurt grecesc",
    "productCategory": "lactate",
    "productBrand": "Lidl",
    "productStore": "lidl",
    "date": "2025-05-05",
    "price": 10.32,
    "currency": "RON"
  }
]
```


  

  
