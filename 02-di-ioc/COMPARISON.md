# IoC & DI 단계별 비교

## 개요

이 문서는 의존성 주입을 구현하는 4가지 단계를 비교합니다. 각 단계는 이전 단계의 문제점을 개선하며, 최종적으로 Spring Framework의 DI가 왜 필요한지 이해하게 됩니다.

## 공통 도메인

모든 단계에서 동일한 도메인을 사용합니다.

```kotlin
// 사용자 저장소 인터페이스
interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
}

// 알림 서비스 인터페이스
interface NotificationService {
    fun send(message: String)
}

// 사용자 서비스 (비즈니스 로직)
class UserService(
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
) {
    fun registerUser(name: String, email: String): User {
        val user = User(name = name, email = email)
        val saved = userRepository.save(user)
        notificationService.send("Welcome ${user.name}!")
        return saved
    }
}
```

## Level 1: Manual DI (수동 의존성 주입)

### 코드

```kotlin
fun main() {
    // 문제: 모든 것을 직접 생성하고 연결
    val repository = UserRepositoryImpl()
    val notificationService = EmailNotificationService()
    val userService = UserService(repository, notificationService)

    userService.registerUser("John", "john@example.com")

    // SMS로 변경하려면?
    // 1. 코드를 찾아서
    // 2. EmailNotificationService를 SmsNotificationService로 변경
    // 3. 재컴파일
}
```

### 문제점

**강한 결합**
main 함수가 구체적인 구현체(UserRepositoryImpl, EmailNotificationService)를 직접 알고 있습니다. 구현체를 변경하려면 코드를 직접 수정해야 합니다.

**테스트 어려움**
UserService를 테스트하려면 실제 UserRepositoryImpl과 EmailNotificationService를 사용해야 합니다. Mock 객체를 주입할 수 없습니다.

**재사용성 부족**
같은 UserRepositoryImpl 인스턴스를 여러 곳에서 사용하려면 매번 new로 생성하거나, 전역 변수를 사용해야 합니다.

**설정 분산**
객체 생성 코드가 여기저기 흩어져 있어, 전체 구조를 파악하기 어렵습니다.

### 코드 복잡도

- 객체 생성: 직접 (3줄)
- 의존성 연결: 수동 (3줄)
- 변경 용이성: 낮음 (코드 수정 필요)
- 테스트 용이성: 낮음 (Mock 주입 불가)

## Level 2: Simple Container (간단한 DI 컨테이너)

### 코드

```kotlin
class DIContainer {
    private val instances = mutableMapOf<Class<*>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T) {
        instances[clazz] = instance
    }

    fun <T : Any> get(clazz: Class<T>): T {
        return instances[clazz] as T
            ?: throw IllegalStateException("${clazz.name} not registered")
    }
}

fun main() {
    val container = DIContainer()

    // 객체 생성과 등록을 한 곳에서
    container.register(UserRepository::class.java, UserRepositoryImpl())
    container.register(NotificationService::class.java, EmailNotificationService())

    // 의존성 주입
    val userService = UserService(
        container.get(UserRepository::class.java),
        container.get(NotificationService::class.java)
    )

    userService.registerUser("John", "john@example.com")

    // SMS로 변경하려면?
    // container.register(NotificationService::class.java, SmsNotificationService())
    // 한 줄만 수정하면 됨!
}
```

### 개선점

**중앙 집중화**
모든 객체 생성과 등록이 한 곳(container)에 모여 있어, 전체 구조를 파악하기 쉽습니다.

**인터페이스 기반**
인터페이스 타입으로 등록하고 조회하므로, 구현체 교체가 쉽습니다.

**테스트 개선**
테스트 시 Mock 객체를 컨테이너에 등록할 수 있습니다.

### 남은 문제점

**수동 등록**
여전히 모든 객체를 수동으로 register 해야 합니다.

**생성자 주입 수동**
UserService의 생성자 파라미터를 일일이 get으로 조회해서 전달해야 합니다.

**Singleton 미지원**
같은 타입을 여러 번 get 하면 동일한 인스턴스를 반환하지만, 이를 명시적으로 제어할 수 없습니다.

### 코드 복잡도

- 객체 생성: 컨테이너에 등록 (2줄)
- 의존성 연결: 수동 (3줄)
- 변경 용이성: 중간 (register만 수정)
- 테스트 용이성: 중간 (Mock 등록 가능)

## Level 3: Reflection DI (리플렉션 기반 DI)

### 코드

```kotlin
@Target(AnnotationTarget.CLASS)
annotation class Component

@Target(AnnotationTarget.CONSTRUCTOR)
annotation class Inject

class ReflectionContainer {
    private val singletons = mutableMapOf<Class<*>, Any>()

    fun scan(packageName: String) {
        // 패키지 스캔하여 @Component 찾기
        // 리플렉션으로 생성자 분석
        // 의존성 자동 주입
    }

    fun <T : Any> get(clazz: Class<T>): T {
        return singletons[clazz] as T
    }
}

@Component
class UserRepositoryImpl : UserRepository {
    // 구현...
}

@Component
class EmailNotificationService : NotificationService {
    // 구현...
}

@Component
class UserService @Inject constructor(
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
) {
    // 비즈니스 로직만!
}

fun main() {
    val container = ReflectionContainer()
    container.scan("com.example")

    val userService = container.get(UserService::class.java)
    userService.registerUser("John", "john@example.com")

    // SMS로 변경하려면?
    // EmailNotificationService에서 @Component 제거
    // SmsNotificationService에 @Component 추가
    // 코드 재컴파일만 하면 됨!
}
```

### 개선점

**자동 스캔**
@Component가 붙은 클래스를 자동으로 찾아서 등록합니다.

**자동 주입**
@Inject가 붙은 생성자의 파라미터를 자동으로 분석하여 의존성을 주입합니다.

**Singleton 자동 관리**
한 번 생성된 객체는 재사용됩니다.

**애노테이션 기반**
코드가 깔끔하고 의도가 명확합니다.

### 남은 문제점

**Lazy Loading 없음**
모든 객체가 스캔 시점에 생성됩니다.

**AOP 미지원**
프록시를 통한 부가 기능 추가가 불가능합니다.

**Profile 미지원**
개발/운영 환경에 따른 Bean 분리가 어렵습니다.

**Bean Lifecycle 부족**
초기화/소멸 콜백을 제어하기 어렵습니다.

**순환 참조 미해결**
A가 B를 의존하고 B가 A를 의존하면 무한 루프에 빠집니다.

### 코드 복잡도

- 객체 생성: 자동 (0줄)
- 의존성 연결: 자동 (0줄)
- 변경 용이성: 높음 (@Component만 이동)
- 테스트 용이성: 높음 (Mock도 @Component 가능)

## Level 4: Spring DI

### 코드

```kotlin
@Configuration
class AppConfig {
    // 필요하면 Bean 수동 등록
}

@Repository
class UserRepositoryImpl : UserRepository {
    // 구현...
}

@Service
class EmailNotificationService : NotificationService {
    // 구현...
}

@Service
class UserService(
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
) {
    fun registerUser(name: String, email: String): User {
        // 순수 비즈니스 로직만!
    }
}

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val context = runApplication<Application>(*args)
    val userService = context.getBean(UserService::class.java)
    userService.registerUser("John", "john@example.com")

    // SMS로 변경하려면?
    // @Primary 또는 @Qualifier 사용
    // 또는 Profile로 환경별 분리
}
```

### Spring이 제공하는 모든 것

**완전한 자동화**
- 컴포넌트 스캔
- 자동 의존성 주입
- Singleton 기본, Prototype 지원

**다양한 주입 방식**
- 생성자 주입 (권장)
- Setter 주입
- 필드 주입

**Bean Lifecycle**
- @PostConstruct (초기화)
- @PreDestroy (소멸)
- InitializingBean, DisposableBean

**고급 기능**
- Lazy Loading (@Lazy)
- Profile (@Profile)
- Conditional Bean (@ConditionalOnProperty)
- AOP 완벽 통합
- 순환 참조 자동 감지 및 해결

**테스트 지원**
- @MockBean
- @SpyBean
- TestContext Framework

### 코드 복잡도

- 객체 생성: 자동 (0줄)
- 의존성 연결: 자동 (0줄)
- 변경 용이성: 매우 높음 (설정만 변경)
- 테스트 용이성: 매우 높음 (@MockBean 등)

## 종합 비교표

| 항목 | Manual | Container | Reflection | Spring |
|------|--------|-----------|------------|--------|
| 코드량 | 많음 | 중간 | 적음 | 매우 적음 |
| 객체 생성 | 직접 | 수동 등록 | 자동 스캔 | 자동 스캔 |
| 의존성 주입 | 직접 | 수동 | 자동 | 자동 |
| Singleton | 수동 | 기본 지원 | 지원 | 완벽 지원 |
| Lazy Loading | 없음 | 없음 | 없음 | 지원 |
| AOP | 없음 | 없음 | 없음 | 완벽 지원 |
| Profile | 없음 | 없음 | 없음 | 지원 |
| Lifecycle | 없음 | 없음 | 부분 | 완벽 지원 |
| 순환 참조 | 오류 | 오류 | 오류 | 자동 해결 |
| 테스트 | 어려움 | 가능 | 쉬움 | 매우 쉬움 |
| 학습 곡선 | 낮음 | 낮음 | 중간 | 높음 |
| 프로덕션 사용 | 불가 | 불가 | 불가 | 필수 |

## 실행 시간 비교

동일한 작업(UserService.registerUser 호출)을 수행할 때:

- Manual: 즉시 (컨테이너 없음)
- Container: 즉시 (간단한 Map 조회)
- Reflection: 약간 느림 (최초 스캔 시간)
- Spring: 느림 (최초 ApplicationContext 로딩), 이후 빠름

하지만 실제 애플리케이션에서는 Spring의 추가 기능들(캐싱, 트랜잭션, AOP 등)이 전체 성능을 오히려 향상시킵니다.

## 코드 가독성 비교

### Manual DI
```kotlin
// main 함수에서
val repo = UserRepositoryImpl()
val noti = EmailNotificationService()
val service = UserService(repo, noti)
```
→ 의도는 명확하지만, 규모가 커지면 관리 불가

### Simple Container
```kotlin
container.register(UserRepository::class.java, UserRepositoryImpl())
container.register(NotificationService::class.java, EmailNotificationService())
val service = UserService(container.get(...), container.get(...))
```
→ 중앙 집중화되었지만, 여전히 장황함

### Reflection DI
```kotlin
@Component
class UserService @Inject constructor(...)
```
→ 깔끔하고 의도가 명확함

### Spring DI
```kotlin
@Service
class UserService(...)
```
→ 가장 간결하고, Spring 생태계와 완벽 통합

## 결론

### Manual DI는 언제?
- 학습 목적
- 매우 작은 프로젝트 (클래스 5개 이하)

### Simple Container는 언제?
- 학습 목적
- Spring을 사용할 수 없는 환경

### Reflection DI는 언제?
- 학습 목적
- 경량 프레임워크 개발

### Spring DI는 언제?
- **실제 프로덕션 애플리케이션**
- 팀 프로젝트
- 유지보수가 중요한 프로젝트
- Spring 생태계 활용이 필요한 경우

## 핵심 메시지

Manual DI에서 시작하여 Spring DI까지 발전하는 과정을 직접 구현해봄으로써, Spring이 단순히 "편리한 도구"가 아니라 "필수적인 프레임워크"임을 이해하게 됩니다.

각 단계는 이전 단계의 문제점을 해결하며, 최종적으로 Spring이 제공하는 완벽한 DI 컨테이너의 가치를 깨닫게 됩니다.

---

*작성일: 2026-01-30*
