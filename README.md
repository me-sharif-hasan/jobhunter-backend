# JobHunter Backend

A sophisticated job aggregation and tracking system built with Spring Boot that helps users find and monitor job opportunity across multiple platforms.

## 🚀 Features

- Job aggregation from multiple sources
- Real-time job tracking
- Google Gemini AI integration
- RESTful API endpoints
- JWT-based authentication
- Automated job indexing
- MySQL database integration

## 📋 Prerequisites

- Java JDK 17 or later
- Maven 3.6+
- MySQL 8.0+
- Docker (optional, for containerization)

## 🔧 Environment Variables

Create a `.env` file in the root directory with the following variables:

```env
GEMINI_KEY=your_gemini_api_key
GEMINI_API_URL=your_gemini_endpoint
API_CLIENT_IDS=your_google_client_ids
RELATIONAL_DATABASE_URL=jdbc:mysql://your_host:3306/your_database
DB_USER=your_database_user
DB_PASSWORD=your_database_password
JWT_SECRET_KEY=your_jwt_secret
```

## 🛠️ Installation & Setup

### Local Development Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/jobhunter-backend.git
   cd jobhunter-backend
   ```

2. Configure your database:
   - Create a MySQL database
   - Update the environment variables with your database credentials

3. Build the project:
   ```bash
   ./mvnw clean install
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Docker Setup

1. Build the Docker image:
   ```bash
   docker build -t jobhunter-backend .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 --env-file .env jobhunter-backend
   ```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/iishanto/jobhunterbackend/
│   │   ├── config/          # Application configurations
│   │   ├── domain/          # Business logic and models
│   │   ├── infrastructure/  # External services integration
│   │   ├── scheduled/       # Scheduled tasks
│   │   └── web/            # Controllers and DTOs
│   └── resources/
│       └── application.properties  # Application configuration
```

## 🗄️ Database Schema

### Entity Relationships

```
Jobs
├── jobId (PK)
├── title
├── jobUrl
├── location
├── salary
├── jobType
├── jobCategory
├── jobDescription
├── skillsNeeded
├── experienceNeeded
├── jobPostedDate
├── jobLastDate
└── jobApplyLink

User
├── id (PK)
├── email
├── name
└── subscriptions (1:N)

Site
├── id (PK)
├── name
├── url
└── users (M:N via UserOwnedSite)

Notification
├── id (PK)
├── message
├── userId (FK)
└── timestamp

Subscription
├── id (PK)
├── userId (FK)
└── preferences

UserAppliedJobs
├── id (PK)
├── userId (FK)
├── jobId (FK)
└── applicationDate
```

## 🛠️ Technology Stack

### Backend
- **Framework:** Spring Boot 3.4.3
- **Build Tool:** Maven
- **Database:** MySQL 8.0
- **Security:** 
  - JWT Authentication
  - Spring Security
  - Google OAuth2

### Libraries & Integrations
- **Web Scraping:** JSoup 1.18.1
- **AI Integration:** Google Gemini API
- **Markdown Processing:** Flexmark
- **Utilities:** Google Guava
- **Testing:** JUnit 5, Mockito
- **Documentation:** Swagger/OpenAPI

### Infrastructure
- **Containerization:** Docker
- **Database Migration:** Hibernate DDL Auto-update
- **Push Notifications:** Firebase Cloud Messaging
- **Job Scheduling:** Spring Scheduler

## 🔐 Security

- JWT-based authentication
- Google OAuth integration
- Secure environment variable handling

## 📦 Dependencies

- Spring Boot 3.4.3
- Spring Security
- MySQL Connector
- JSoup 1.18.1
- Flexmark
- Google Guava

## 🚥 API Documentation

The API documentation is available at `/swagger-ui.html` when running the application.

## 👥 Contributing Guidelines

### Development Workflow

1. **Fork & Clone**
   ```bash
   git clone https://github.com/your-username/jobhunter-backend.git
   cd jobhunter-backend
   git remote add upstream https://github.com/original/jobhunter-backend.git
   ```

2. **Branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/your-fix-name
   ```

3. **Code Style**
   - Follow Java naming conventions
   - Use meaningful variable and method names
   - Add JavaDoc comments for public methods
   - Keep methods focused and concise
   - Use proper indentation (4 spaces)

4. **Testing Requirements**
   - Write unit tests for new features
   - Ensure all tests pass before submitting PR
   - Maintain minimum 80% code coverage
   - Include integration tests for API endpoints

5. **Commit Guidelines**
   ```
   feat: add job search filtering
   fix: resolve notification duplicate issue
   docs: update API documentation
   test: add tests for subscription service
   refactor: improve job matching algorithm
   ```

6. **Pull Request Process**
   - Keep PRs focused on a single feature/fix
   - Provide clear PR description
   - Link related issues
   - Add screenshots for UI changes
   - Ensure CI checks pass
   - Request review from maintainers

### Setting Up Development Environment

1. **IDE Setup**
   - Recommended: IntelliJ IDEA
   - Install Lombok plugin
   - Enable annotation processing

2. **Database Setup**
   ```bash
   # Create development database
   mysql -u root -p
   CREATE DATABASE jobhunter_dev;
   ```

3. **Environment Configuration**
   - Copy `.env.example` to `.env`
   - Configure local development values
   - Never commit sensitive credentials

4. **Running Tests**
   ```bash
   ./mvnw clean test
   ```

5. **Local Development**
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=dev
   ```

### Debugging Tips
- Use application-dev.properties for local settings
- Enable debug logging when needed
- Use H2 console for database inspection
- Spring Boot Actuator endpoints available at /actuator/*

## 🛟 Support

For support and questions, please open an issue in the repository.

## 📜 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

---

Made with ❤️ by the JobHunter Team
