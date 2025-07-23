# Project Structure

## Architecture Pattern
This project follows **Hexagonal Architecture** (Ports and Adapters) with clear separation of concerns across modules.

## Module Organization

### Core Modules
- **`core/domain`**: Pure domain models and business entities
  - Contains domain objects like `Challenge`, `User`, `ChatSession`, `Lesson`
  - No external dependencies, pure Kotlin classes
  
- **`core/application`**: Business logic and use cases
  - Service classes implementing business rules
  - Input/Output DTOs for use cases
  - Domain service interfaces

### Adapter Modules
- **`adapters/web`**: REST API controllers and web configuration
  - Controllers organized by feature (`auth`, `challenge`)
  - Request/Response DTOs
  - Authentication and error handling
  
- **`adapters/persistence`**: Database access layer
  - Repository adapters implementing domain interfaces
  - Entity classes and mappers for MongoDB
  - Database configuration
  
- **`adapters/ai`**: AI service integration
  - OpenAI integration for chat functionality
  
- **`adapters/messaging`**: Message handling (future use)

### Application Module
- **`application`**: Main Spring Boot application
  - Application entry point
  - Configuration files (application.yml variants)
  - Dependency wiring

## Package Structure Convention
```
lab.ujumeonji.moco/
├── model/           # Domain models and services
├── adapter/         # Repository adapters
├── controller/      # REST controllers
├── config/          # Configuration classes
├── support/         # Utility classes
└── repository/      # Data access interfaces
```

## Key Patterns
- **Repository Pattern**: Domain interfaces implemented by adapter classes
- **Mapper Pattern**: Separate mappers convert between domain and entity objects
- **DTO Pattern**: Input/Output classes for API boundaries
- **Service Layer**: Business logic encapsulated in service classes

## File Naming Conventions
- Domain models: Simple names (`Challenge.kt`, `User.kt`)
- Services: `*Service.kt` 
  - DTOs: `*Input.kt`, `*Output.kt`
- Controllers: `*Controller.kt`
  - DTOs: `*Request.kt`, `*Response.kt`
- Repositories: `*Repository.kt` (interfaces), `*RepositoryAdapter.kt` (implementations)
- Entities: `*Entity.kt`
- Mappers: `*Mapper.kt`