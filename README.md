# CoffeeShop

Coffee shop menu web app with public catalog and admin management.

## Features
- Public menu with language switch (ru/en)
- Category and item management in the admin panel
- Image upload for categories and items
- REST endpoints for admin and images

## Tech Stack
- Java 17, Spring Boot 3.5
- Thymeleaf
- PostgreSQL
- Maven

## Requirements
- Java 17
- PostgreSQL
- Maven (or use the Maven wrapper)

## Configuration
The app reads env vars from a local `.env` file if present.

Key variables (defaults in `src/main/resources/application.properties`):
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- ADMIN_USER
- ADMIN_PASS
- FILE_UPLOAD_DIR

Default admin credentials: admin / admin123

## Run

### Option 1: Docker (recommended)

Run everything in Docker:

```bash
docker-compose up --build
```

### Option 2: Local development with Docker DB

Start only PostgreSQL in Docker:

```bash
docker-compose -f docker-compose.dev.yml up -d
```

Then run the app locally:

Windows:
```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5433/coffeeshop_db"
$env:SPRING_DATASOURCE_PASSWORD="postgres"
.\mvnw.cmd spring-boot:run
```

Linux/macOS:
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/coffeeshop_db \
SPRING_DATASOURCE_PASSWORD=postgres \
./mvnw spring-boot:run
```

### Option 3: Local PostgreSQL

Windows:
```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:
```bash
./mvnw spring-boot:run
```

## URLs
- http://localhost:8080/
- http://localhost:8080/admin/categories
- http://localhost:8080/admin/items
- http://localhost:8080/login

## Screenshots
![Overview 1](docs/screenshots/overview-1.png)
![Overview 2](docs/screenshots/overview-2.png)
![Overview 3](docs/screenshots/overview-3.png)
![Overview 4](docs/screenshots/overview-4.png)
