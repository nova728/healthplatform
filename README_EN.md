# Health Management Platform - Backend

English | [简体中文](./README.md)

## Project Overview

The backend project of Health Management Platform, a RESTful API service built with Spring Boot. Provides complete user management, health data management, community interaction, and other core functions, supporting high concurrency access and real-time message pushing.

## Tech Stack

<p align="center">
<img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
<img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security">
<img src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=mybatis&logoColor=white" alt="MyBatis">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">
<img src="https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socket.io&logoColor=white" alt="WebSocket">
<img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven">
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
</p>

### Core Technologies

- **Spring Boot 3.2.0** - Framework for rapid production-ready application development
- **Spring Security** - Authentication and authorization functionality
- **MyBatis** - Excellent persistence framework
- **MySQL 8.0** - Relational database
- **Redis** - Caching and session management
- **WebSocket** - Real-time message pushing
- **JWT** - Stateless authentication

### Third-party Integrations

- **ERNIE Bot API** - AI health consultation
- **FatSecret Platform API** - Food nutrition data
- **OpenFDA API** - Drug information lookup

## Key Features

- **User Management** - Registration, login, authentication, authorization
- **Health Data Management** - Heart rate, blood pressure, weight, BMI, sleep tracking
- **Exercise Management** - Activity logging, goal management, leaderboards
- **Diet Management** - Nutrition tracking, food database
- **Medication Management** - Medicine records, reminder services
- **Health Reports** - Intelligent analysis, scoring, recommendation generation
- **Community Forum** - Article management, comments, likes, favorites
- **Real-time Notifications** - WebSocket message pushing

## Quick Start

### Prerequisites

- JDK 17+
- Maven 3.9.9+
- MySQL 8.0+
- Redis 6.0+ (optional)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/nova728/healthplatform
cd healthplatform
```

2. **Configure database**

Create database:
```sql
CREATE DATABASE healthplatform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Modify database configuration in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/healthplatform?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

3. **Configure third-party APIs**

Configure API keys in `application.yml`:
```yaml
api:
  ernie-bot:
    api-key: your_ernie_bot_api_key
  fatsecret:
    consumer-key: your_fatsecret_consumer_key
    consumer-secret: your_fatsecret_consumer_secret
  openfda:
    base-url: https://api.fda.gov
```

4. **Start application**
```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8080`.

### Build Project

```bash
mvn clean package
```

The generated JAR file is located in the `target/` directory.

## Project Structure

```
healthplatform/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/healthplatform/
│   │   │       ├── controller/      # Controller layer
│   │   │       ├── service/         # Business logic layer
│   │   │       ├── mapper/          # Data access layer
│   │   │       ├── entity/          # Entity classes
│   │   │       ├── dto/             # Data transfer objects
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── security/        # Security configuration
│   │   │       ├── websocket/       # WebSocket configuration
│   │   │       └── util/            # Utility classes
│   │   └── resources/
│   │       ├── mapper/              # MyBatis XML mapping files
│   │       ├── application.yml      # Application configuration
│   │       └── application-dev.yml  # Development environment config
│   └── test/                        # Test code
├── pom.xml                          # Maven configuration
└── README.md
```

## API Documentation

### User Module

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/user/profile` - Get user information
- `PUT /api/user/profile` - Update user information

### Health Data Module

- `POST /api/health/record` - Add health record
- `GET /api/health/records` - Get health records list
- `GET /api/health/report` - Generate health report
- `GET /api/health/statistics` - Get health statistics

### Exercise Module

- `POST /api/exercise/record` - Add exercise record
- `GET /api/exercise/records` - Get exercise records
- `POST /api/exercise/goal` - Set exercise goals
- `GET /api/exercise/leaderboard` - Get leaderboard

### Diet Module

- `POST /api/diet/record` - Add diet record
- `GET /api/diet/records` - Get diet records
- `GET /api/diet/nutrition` - Get nutrition analysis
- `GET /api/diet/food/search` - Search food

### Medication Module

- `POST /api/medication/record` - Add medication record
- `GET /api/medication/records` - Get medication records
- `POST /api/medication/reminder` - Set medication reminder
- `GET /api/medication/info` - Query drug information

### Community Module

- `POST /api/forum/article` - Publish article
- `GET /api/forum/articles` - Get articles list
- `POST /api/forum/comment` - Post comment
- `POST /api/forum/like` - Like
- `POST /api/forum/favorite` - Favorite

## Technical Highlights

### Security

- **JWT Authentication** - Stateless authentication mechanism
- **Password Encryption** - BCrypt encrypted storage
- **Permission Control** - Role-based access control (RBAC)
- **XSS Protection** - Input validation and output encoding
- **SQL Injection Protection** - Prepared statements

### Performance Optimization

- **Redis Cache** - Cache hot data
- **Database Connection Pool** - HikariCP high-performance connection pool
- **Asynchronous Processing** - @Async annotation for async execution
- **Pagination** - MyBatis PageHelper pagination
- **Index Optimization** - Database index optimization

### High Availability

- **Exception Handling** - Global exception handling mechanism
- **Logging** - SLF4J + Logback logging framework
- **Health Checks** - Spring Boot Actuator
- **Transaction Management** - Declarative transactions

## Database Design

### Core Table Structure

- `user` - User table
- `health_record` - Health records table
- `exercise_record` - Exercise records table
- `diet_record` - Diet records table
- `medication_record` - Medication records table
- `article` - Articles table
- `comment` - Comments table
- `notification` - Notifications table

## Development Notes

### Code Standards

The project follows Alibaba Java Development Guidelines and uses CheckStyle for code checking.

### Unit Testing

```bash
mvn test
```

### API Testing

We recommend using Postman or Swagger UI for API testing.

Swagger UI address: `http://localhost:8080/swagger-ui.html`

## Deployment

### Docker Deployment

```bash
# Build image
docker build -t healthplatform:latest .

# Run container
docker run -d -p 8080:8080 healthplatform:latest
```

### Environment Configuration

For production environment, use `application-prod.yml` configuration file and ensure:

- Modify database connection configuration
- Configure Redis connection
- Update API keys
- Enable HTTPS
- Configure log levels

## Contributing

Issues and Pull Requests are welcome!

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Related Links

- Frontend Repository: [healthplatform_front](https://github.com/nova728/healthplatform_front)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Contact

For questions or suggestions, please contact us through Issues.

---

⭐ If this project helps you, please give us a Star!
