# Customer Management API

## Run Locally
```bash
./mvnw spring-boot:run
```

## H2 Console
```
http://localhost:8080/h2-console
```
(JDBC URL: `jdbc:h2:mem:customerdb`, username: `test`)

## Swagger
http://localhost:8080/swagger-ui.html

## Sample Requests
- POST /customers
- GET /customers/{id}
- GET /customers?email=...
- PUT /customers/{id}
- DELETE /customers/{id}

## Assumptions
- Tier is derived dynamically and not stored
- UUID is generated on creation only