## 1. Package Structure
- **adapters**: Communication with external systems
  - `web`: Web API (controllers, DTOs)
  - `persistence`: Database implementations

- **core**: Core business logic
  - `application`: Service layer
  - `domain`: Domain models

## 2. Layer Separation Principles
### 2.1 DTO Layer
- **Request/Response DTOs**
  - Location: `adapters/web/.../dto`
  - Naming: `{Action}{Resource}Request/Response`
  - Role: Transfer HTTP request/response data

- **Input/Output DTOs**
  - Location: `core/application/.../io`
  - Naming: `{Action}{Resource}Input/Output`
  - Role: Transfer business logic input/output data

### 2.2 Conversion Rules
- Request → Input: Implement `toInput()`
- Input → Domain: Implement `toDomain()` (with `internal` visibility)
- Domain → Output: Implement `fromDomain()` (using companion object)

## 3. REST API Rules
### 3.1 Endpoints
- Base URL: Plural nouns (e.g., `/challenges`)
- HTTP Methods:
  - GET: Retrieve
  - POST: Create
  - PUT: Update (full)
  - PATCH: Update (partial)
  - DELETE: Remove

### 3.2 Response Handling
- Response Format: Use `ResponseEntity<T>`
- Status Codes:
  - 200: Success
  - 201: Created
  - 204: Deleted
  - 404: Resource not found

### 3.3 Pagination
- Use `Page<T>`
- Default Parameters:
  - page: Page number (default: 0)
  - size: Page size (default: 10)
  - sort: Sort condition (e.g., `createdAt,desc`)

## 4. Validation
- Use Jakarta Validation
- Common Annotations:
  - `@NotBlank`: Required string
  - `@Email`: Email format
  - `@Size`: Length constraints

- Define all validation messages explicitly

## 5. Service Layer Rules
### 5.1 Method Naming
- Basic retrieval: `findBy{Field}`
- Output data conversion: `findBy{Field}Output`
- Creation/Update: `save`, `saveOutput`

### 5.2 Logging
- Use SLF4J Logger
- Log important operations (INFO level)
- Log error situations (ERROR level)

## 6. Code Style
- Follow Kotlin naming conventions
- Use explicit type declarations
- Utilize data classes extensively
- Nullable types: Explicitly use `?`
- Companion object: Use for static factory methods

## 7. Testing
- Write unit tests for each layer
- Integration tests only for essential scenarios
- Test naming: Use `Given_When_Then` format
