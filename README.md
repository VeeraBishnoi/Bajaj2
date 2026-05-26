# BFHL API — Bajaj Finserv Health Hiring Round

A production-ready Spring Boot REST API built for the Bajaj Finserv Health hiring challenge.

## Candidate

| Field | Value |
|---|---|
| Name | Veera Bishnoi |
| Registration Number | 0827AL231144 |
| Email | veerabishnoi231058@acropolis.in |

---

## Tech Stack

- Java 21
- Spring Boot 3.2.5
- Maven
- JUnit 5 + Mockito

---

## Project Structure

```
src/
├── main/java/com/bajaj/bfhl/
│   ├── BfhlApiApplication.java       # Entry point
│   ├── controller/
│   │   └── BfhlController.java       # REST endpoints
│   ├── service/
│   │   ├── BfhlService.java          # Interface
│   │   └── BfhlServiceImpl.java      # Business logic
│   ├── dto/
│   │   ├── BfhlRequest.java          # Request DTO
│   │   └── BfhlResponse.java         # Response DTO
│   └── exception/
│       └── GlobalExceptionHandler.java
└── test/java/com/bajaj/bfhl/
    ├── controller/BfhlControllerTest.java
    └── service/BfhlServiceImplTest.java
```

---

## Endpoints

### POST /bfhl

**Request:**
```json
{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

**Response:**
```json
{
  "is_success": true,
  "user_id": "veera_bishnoi_26052026",
  "email": "veerabishnoi231058@acropolis.in",
  "roll_number": "0827AL231144",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

### GET /health

**Response:**
```json
{ "status": "UP" }
```

---

## Business Logic

| Field | Logic |
|---|---|
| `odd_numbers` | Numeric strings where value is odd |
| `even_numbers` | Numeric strings where value is even |
| `alphabets` | Alpha-only strings, uppercased |
| `special_characters` | Non-alphanumeric strings |
| `sum` | Sum of all numeric values, returned as string |
| `concat_string` | All alpha chars combined → reversed → alternating caps (starts uppercase) |

---

## Running Locally

```bash
./mvnw spring-boot:run
```

Or with Maven:

```bash
mvn spring-boot:run
```

The server starts on port **8080** by default.

---

## Running Tests

```bash
./mvnw test
```

---

## Building the JAR

```bash
./mvnw clean package -DskipTests
java -jar target/bfhl-api-1.0.0.jar
```
