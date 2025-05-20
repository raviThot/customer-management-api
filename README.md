# Customer Management API

A Spring Boot-based RESTful service that provides CRUD operations for customer records. Customers are categorized into tiers (Silver, Gold, Platinum) based on their annual spend and last purchase date.

---

## 🧱 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- H2 In-Memory Database
- Lombok
- Springdoc OpenAPI (Swagger UI)

---

## 🚀 How to Build & Run the Application

### 📦 Prerequisites

- Java 21+
- Maven 3.8+

### 🔨 Build

```bash
mvn clean install
```

### ▶ Run

```bash
mvn spring-boot:run
```

The application will start at:  
📍 `http://localhost:8080`

---

## 📘 Swagger API Documentation

Interactive API documentation is available at:

📄 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔌 Sample Requests

### 1. ✅ Create Customer

```http
POST /api/v1/customers
Content-Type: application/json

{
  "name": "Alice",
  "email": "alice@example.com",
  "annualSpend": 15000,
  "lastPurchaseDate": "2024-12-01T00:00:00"
}
```

---

### 2. 🔍 Get Customer by ID

```
GET /api/v1/customers/{id}
```

---

### 3. 🔍 Get Customer by Name

```
GET /api/v1/customers?name=Alice
```

---

### 4. 🔍 Get Customer by Email

```
GET /api/v1/customers?email=alice@example.com
```

---

### 5. 🔄 Update Customer

```http
PUT /api/v1/customers/{id}
Content-Type: application/json

{
  "name": "Alice Updated",
  "email": "alice.updated@example.com",
  "annualSpend": 8000,
  "lastPurchaseDate": "2025-01-01T00:00:00"
}
```

---

### 6. ❌ Delete Customer

```
DELETE /api/v1/customers/{id}
```

---

## 🗄 Accessing the H2 Database Console

H2 console is enabled at:

🔗 `http://localhost:8080/h2-console`

### 🧾 Login Info:

- **JDBC URL**: `jdbc:h2:mem:customerdb`
- **Username**: `sa`
- **Password**: *(leave blank)*

---

## 📝 Assumptions

- UUID is used as a unique identifier for customers.
- Tier is calculated on-the-fly based on annual spend and last purchase date:
    - **Platinum**: spend ≥ 10,000 and purchased in the last 6 months
    - **Gold**: spend ≥ 1,000 and purchased in the last 12 months
    - **Silver**: everyone else
- Only basic validation is applied (`@NotBlank`, `@Email`).
- No authentication or authorization is implemented for simplicity.
- Email and name lookups return a single customer.

---