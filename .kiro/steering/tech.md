# Technology Stack

## Core Technologies
- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.2.5
- **Java Version**: 21 (Eclipse Temurin)
- **Build System**: Gradle with Kotlin DSL
- **Database**: MongoDB with Spring Data MongoDB
- **AI Integration**: Spring AI with OpenAI
- **Authentication**: JWT tokens
- **Testing**: JUnit 5, Kotest, Spring Boot Test

## Key Dependencies
- Jackson for JSON processing
- Spring Boot Actuator for monitoring
- Spring Boot DevTools for development
- Docker support via Jib plugin

## Code Quality
- **Formatter**: Spotless with ktlint 1.0.1
- **Style**: 4 spaces indentation, trailing whitespace trimmed, newline at EOF

## Common Commands

### Build & Run
```bash
./gradlew build                 # Build all modules
./gradlew bootRun              # Run the application
./gradlew test                 # Run all tests
./gradlew spotlessApply        # Format code
./gradlew spotlessCheck        # Check code formatting
```

### Docker
```bash
./gradlew jib                  # Build and push Docker image
./gradlew jibDockerBuild       # Build Docker image locally
```

### Development
```bash
./gradlew bootRun --args='--spring.profiles.active=local'  # Run with local profile
```

## Environment Variables
- `OPENAI_API_KEY`: OpenAI API key for AI features
- `JWT_SECRET_KEY`: Secret key for JWT token signing
- `PORT`: Application port (default: 8080)
- `APP_PROFILE`: Spring profile to activate