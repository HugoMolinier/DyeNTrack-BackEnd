<div align="center">

# 🏋️ DyeNTrack — Backend API

### A REST API for fitness and personal progress tracking

**Java · Spring Boot · Spring Security · JWT · MySQL · Docker**

<br />

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.1-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

<br />

**Part of the DyeNTrack mobile application project.**

[Portfolio](https://molinier-hugo.vercel.app/) · [Repository](https://github.com/HugoMolinier/DyeNTrack-BackEnd)

</div>

---

## 📖 Overview

DyeNTrack Backend is the server-side component of **DyeNTrack**, a mobile application designed to help users track their fitness activities and personal progress.

Built with Java and Spring Boot, the backend provides a REST API and integrates authentication, database persistence, email services and API documentation.

The application is containerized with Docker to simplify deployment and environment setup.

## ✨ Key Technologies

| Technology | Purpose |
|---|---|
| Java 21 | Main programming language |
| Spring Boot 3.5.7 | Backend application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database persistence and ORM |
| Spring Security | Application security |
| JSON Web Token (JWT) | Token-based authentication |
| Spring Mail | Email integration |
| MySQL 8.1 | Relational database |
| H2 Database | Lightweight runtime database option |
| Springdoc OpenAPI | API documentation and Swagger UI |
| Apache POI | Excel document processing |
| Docker & Docker Compose | Containerization and deployment |
| Maven | Dependency management and build automation |

## 🏗️ Architecture

The backend follows a conventional Spring Boot application structure.

```text
DyeNTrack-BackEnd/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   └── resources/
│   └── test/
├── server_script/
├── .mvn/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

The application uses Spring Boot components to expose HTTP endpoints, enforce security rules and interact with the database.

- **REST API:** HTTP endpoints for client-server communication.
- **Security:** Spring Security and JWT-based authentication.
- **Persistence:** Spring Data JPA with a relational database.
- **Email:** Spring Mail integration.
- **API documentation:** OpenAPI and Swagger UI.
- **Deployment:** Docker containers orchestrated with Docker Compose.

## 🔐 Security

The project integrates Spring Security and JSON Web Tokens (JWT) to support token-based authentication.

The security stack includes:

- Spring Security for request protection.
- JWT libraries for token-related operations.
- Configurable authentication and authorization rules.
- Secure configuration through environment variables.

Authentication endpoints, token expiration policies and authorization rules depend on the application's implementation.

## 🗄️ Database

The project supports relational database persistence through Spring Data JPA.

The Docker Compose configuration provides a MySQL database and a phpMyAdmin interface for database administration.

**Database components:**

- MySQL for persistent relational storage.
- Spring Data JPA for ORM and data access.
- H2 as an additional database dependency.
- phpMyAdmin for database administration.

## 🚀 Getting Started

### Prerequisites

Make sure you have installed:

- [Java 21](https://www.oracle.com/java/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- Docker Compose

Maven is included through the Maven Wrapper, so a separate Maven installation is not required.

### 1. Clone the repository

```bash
git clone https://github.com/HugoMolinier/DyeNTrack-BackEnd.git
cd DyeNTrack-BackEnd
```

### 2. Configure environment variables

Create a `.env` file in the project root directory.

Configure the database and application settings required by the project.


### 3. Start the application with Docker

Build and start the containers:

```bash
docker compose up --build -d
```

Check the running containers:

```bash
docker compose ps
```

View the application logs:

```bash
docker compose logs -f app
```

Stop the environment:

```bash
docker compose down
```

The Docker Compose configuration defines the following services:

| Service | Port | Purpose |
|---|---:|---|
| Application | `8080` | Spring Boot REST API |
| phpMyAdmin | `8081` | Database administration |
| MySQL | Configurable | Relational database |

The database port mapping and application connection settings must match your local configuration.

### 4. Run the application locally

Alternatively, run the application directly using the Maven Wrapper.

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Make sure the required environment variables and database connection are configured before starting the application.

## 📚 API Documentation

The project integrates Springdoc OpenAPI to generate API documentation.

Once the application is running, try accessing Swagger UI:

**http://localhost:8080/swagger-ui/index.html**

The OpenAPI specification is generally available at:

**http://localhost:8080/v3/api-docs**

These endpoints depend on the application's configuration and security rules.

Swagger UI allows developers to explore the available endpoints and inspect their request and response schemas.

## 🧪 Testing

Run the test suite using the Maven Wrapper.

**Linux / macOS**

```bash
./mvnw test
```

**Windows**

```powershell
.\mvnw.cmd test
```

## 🐳 Docker Deployment

The project includes a `Dockerfile` and a `docker-compose.yml` configuration.

Docker Compose orchestrates the application and its database dependencies, providing a reproducible environment for development and deployment.

The application image is configured in the Compose file as:

```text
molinierhugo/api-dyentrack:latest
```

For production deployments, use explicit image versions, secure secrets management and an appropriately configured database.

## 🔧 Development

Build the application:

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
