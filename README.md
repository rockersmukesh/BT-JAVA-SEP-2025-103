
# Trip Management

A comprehensive REST API for managing travel trips with features including CRUD operations, searching, filtering, and trip statistics.


## Technologies Used

- Java 21
- Spring Boot 3.x
- Hibernate (via Spring Data JPA)
- Maven
- MySQL Database
- JUnit 5 + Mockito
- Swagger/OpenAPI for API documentation


## Setup and Installation

### Prerequisites

- JDK 17 or later
- Maven 3.6+
- MySQL 8.0+
- Spring Tool Suite/Intellij

### Steps to Run the Application
1. Clone the repository
```
 git clone https://github.com/yourusername/trip-management.git
```
```
cd trip-management
```
2. Configure database settings

Edit
src/main/resources/application.properties with your MySQL credentials:
```
spring.datasource.url=jdbc:mysql://localhost:3306/trip_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
3. Create the database schema / or not need hibernate automatically creates
   Run the tripdb.sql script in your MySQL client:
```
mysql -u your_username -p < tripdb.sql
```
4. Access the API documentation
```
http://localhost:8080/swagger-ui.html
```
## API Endpoints with Examples

#### Create a Trip

```http
POST /api/trips
```

Request Body:
```
{
  "destination": "Paris",
  "startDate": "2024-08-15",
  "endDate": "2024-08-25",
  "price": 2500.00,
  "status": "PLANNED"
}
```

Response
```
{
  "statusCode": "CREATED",
  "message": "Trip created successfully",
  "data": {
    "id": 11,
    "destination": "Paris",
    "startDate": "2024-08-15",
    "endDate": "2024-08-25",
    "price": 2500.00,
    "status": "PLANNED"
  }
}
```

#### Get All Trips
```http
GET /api/trips/all
Eg. GET /api/trips/all
```
Response
```
{
  "statusCode": "OK",
  "message": "All trips fetched successfully",
  "data": [
    {
      "id": 1,
      "destination": "Paris",
      "startDate": "2024-06-01",
      "endDate": "2024-06-10",
      "price": 1500.00,
      "status": "PLANNED"
    },
    {
      "id": 2,
      "destination": "London",
      "startDate": "2024-07-15",
      "endDate": "2024-07-25",
      "price": 2000.00,
      "status": "PLANNED"
    }
    // More trips...
  ]
}
```

#### Get Paginated Trips
```
GET /api/trips?page=0&size=5&sort=price,desc
```

#### Get Trip by ID

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `int` | **Required**. trips Id |

```http
GET /api/trips/${1}
Eg. GET /api/trips/1
```

#### Update Trip
```http
PUT /api/trips/${1}
Eg. PUT /api/trips/1
```
Request Body:
```
{
  "destination": "Paris",
  "startDate": "2024-06-05",
  "endDate": "2024-06-15",
  "price": 1600.00,
  "status": "PLANNED"
}
```

#### Delete Trip
```http
DELETE /api/trips/${1}
eg. DELETE /api/trips/1
```

#### Search Trips by Destination
```
GET /api/trips/search?destination=Paris
```

#### Filter Trips by Status
```
GET /api/trips/filter?status=PLANNED
```

#### Get Trips by Date Range
```
GET /api/trips/daterange?start=2024-06-01&end=2024-08-31
```

#### Get Trip Summary
```http
GET /api/trips/summary
```
Response
```
{
  "statusCode": "OK",
  "message": "Trip summary found successfully",
  "data": {
    "totalTrips": 10,
    "minPrice": 1500.00,
    "maxPrice": 4000.00,
    "averagePrice": 2180.00
  } 
```
