# Moco-Backend 코드 리뷰 가이드라인

이 문서는 Moco-Backend 프로젝트의 코드 리뷰를 위한 가이드라인을 제공합니다. 일관성 있고 유지보수 가능한 코드를 작성하기 위해 모든 기여자는 이 가이드라인을 숙지하고 따라야 합니다.

## 1. 아키텍처

Moco-Backend는 **Hexagonal Architecture (Ports and Adapters)** 를 기반으로 설계되었습니다. 이 아키텍처는 비즈니스 로직(Domain, Application)과 외부 기술(Web, Persistence)을 분리하여 유연하고 테스트하기 쉬운 구조를 만드는 것을 목표로 합니다.

### 모듈 구조

- `core`: 프로젝트의 핵심 비즈니스 로직을 포함합니다.
    - `core:domain`: 순수한 비즈니스 모델과 규칙을 정의합니다. 외부 의존성이 없어야 합니다.
    - `core:application`: 도메인 모델을 사용하여 실제 비즈니스 로코드를 구현하는 서비스 계층입니다. Port를 통해 외부 어댑터와 통신합니다.
- `adapters`: 외부 기술과의 연동을 담당합니다.
    - `adapters:web`: Spring Web MVC를 사용한 API 컨트롤러를 포함합니다.
    - `adapters:persistence`: Spring Data MongoDB를 사용한 데이터베이스 연동 로직을 포함합니다.
    - `adapters:messaging`: 메시징 시스템 연동을 담당합니다. (구현 예정)
    - `adapters:ai`: AI 모델 연동을 담당합니다. (구현 예정)
- `application`: 최종 애플리케이션을 구성하고 실행하는 역할을 합니다.

## 2. 코딩 스타일

### 일반

- **언어**: Kotlin을 주력 언어로 사용합니다.
- **코딩 컨벤션**: [Kotlin 공식 코딩 컨벤션](https://kotlinlang.org/docs/coding-conventions.html)을 따릅니다.
- **네이밍**:
    - 클래스: `PascalCase` (e.g., `ChallengeService`)
    - 함수/변수: `camelCase` (e.g., `createChallenge`)
    - 상수: `UPPER_SNAKE_CASE` (e.g., `MAX_RETRY_COUNT`)
- **불변성**: `val`을 기본으로 사용하고, 변경이 필요한 경우에만 `var`를 사용합니다. 데이터 클래스와 컬렉션은 불변성을 유지하도록 노력합니다.
- **로깅**: SLF4J를 사용하여 로그를 기록합니다. 각 클래스에 `LoggerFactory`를 통해 로거를 생성하여 사용합니다.

```kotlin
private val logger = LoggerFactory.getLogger(UserService::class.java)
```

### 예외 처리

- 비즈니스 로직 상 예상 가능한 오류는 명시적인 커스텀 예외를 정의하여 사용합니다. (e.g., `EmailAlreadyExistsException`)
- `@RestControllerAdvice`를 사용한 `GlobalExceptionHandler`에서 커스텀 예외를 처리하여 일관된 오류 응답을 반환합니다.

## 3. 계층별 가이드라인

### `core:domain`

- 순수한 Kotlin 클래스로 도메인 모델(`User`, `Challenge` 등)을 정의합니다.
- 어떠한 프레임워크나 외부 라이브러리에도 의존하지 않아야 합니다. (Java/Kotlin 표준 라이브러리 제외)
- 도메인 모델은 상태와 핵심 비즈니스 규칙을 캡슐화합니다.

```kotlin
// Good Example
class User(
    val id: String,
    val name: String,
    // ...
)
```

### `core:application`

- 비즈니스 로직을 처리하는 서비스 클래스(`*Service`)를 포함합니다.
- `@Service` 어노테이션을 사용하여 Spring Bean으로 등록합니다.
- **Port**: 외부와의 통신을 위한 인터페이스를 정의합니다. (e.g., `UserRepositoryPort`)
- 서비스는 Port 인터페이스에 의존하며, 실제 구현체(Adapter)는 DI를 통해 주입받습니다.
- DTO(Data Transfer Object)를 사용하여 계층 간 데이터 전송을 명확히 합니다. Input/Output Record 클래스를 활용합니다.

```kotlin
// UserService.kt
@Service
class UserService(
    private val userRepositoryAdapter: UserRepositoryAdapter, // Port 대신 Adapter 직접 의존 (개선 가능 지점)
    private val passwordEncoder: PasswordEncoder,
) {
    fun signUp(request: SignUpInput): User {
        // ...
    }
}
```

> **참고**: 현재 코드는 Port 인터페이스 없이 Adapter를 직접 주입받고 있습니다. 향후 리팩토링을 통해 Port를 도입하여 의존성을 더욱 낮추는 것을 고려할 수 있습니다.

### `adapters`

- 특정 기술 구현을 담당합니다.
- **Web Adapter**:
    - `@RestController`를 사용하여 API 엔드포인트를 정의합니다.
    - `Application` 서비스에 요청을 위임하고, 결과를 받아 HTTP 응답으로 변환합니다.
    - 요청/응답 데이터는 DTO(`*Request`, `*Response`)를 사용합니다.
- **Persistence Adapter**:
    - Port 인터페이스를 구현합니다. (현재는 Adapter 클래스로 직접 구현)
    - Spring Data Repository(`*Repository`)와 Mapper(`*Mapper`)를 사용하여 데이터베이스와 상호작용합니다.
    - 도메인 모델과 데이터베이스 엔티티(`*Entity`)를 분리하고, Mapper를 통해 상호 변환합니다.

```kotlin
// UserRepositoryAdapter.kt
@Component
class UserRepositoryAdapter(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {
    fun save(user: User): User {
        val entity = userMapper.toEntity(user)
        val savedEntity = userRepository.save(entity)
        return userMapper.toDomain(savedEntity)
    }
}
```

## 4. 테스트

- **단위 테스트**: `core` 모듈의 서비스와 도메인 모델을 중심으로 작성합니다. Mocking 라이브러리(e.g., MockK)를 사용하여 의존성을 격리합니다.
- **통합 테스트**: `adapters` 모듈을 포함하여 실제 환경과 유사하게 테스트합니다. `@SpringBootTest`를 사용하여 전체 애플리케이션 컨텍스트를 로드하여 테스트합니다.
- **테스트 컨벤션**: `given-when-then` 구조를 따라 테스트 케이스를 명확하게 작성합니다.

## 5. Git & Pull Request

- **브랜치 전략**: `main` 브랜치를 중심으로 기능 개발은 `feature/` 브랜치에서 진행합니다. (e.g., `feature/add-new-api`)
- **커밋 메시지**: [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) 스타일을 따릅니다.
- **Pull Request**:
    - PR 제목은 커밋 메시지와 동일한 스타일로 작성합니다.
    - PR 본문에는 변경 사항에 대한 상세한 설명과 테스트 방법을 포함합니다.
    - 최소 1명 이상의 리뷰어에게 승인을 받아야 머지할 수 있습니다.
    - CI 빌드가 성공해야 합니다.
