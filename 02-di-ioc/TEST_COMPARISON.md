# 테스트 코드 비교

4가지 레벨에서 **동일한 테스트 시나리오**가 실행되어 **동일한 결과**를 보장합니다.

## 테스트 메서드 비교

모든 레벨에서 다음 5개의 동일한 테스트를 실행합니다:

| 테스트 메서드 | Level 1 | Level 2 | Level 3 | Level 4 |
|------------|---------|---------|---------|---------|
| `registerUser should assign id automatically` | ✓ | ✓ | ✓ | ✓ |
| `getUser should return user by id` | ✓ | ✓ | ✓ | ✓ |
| `getUser should return null for non-existent id` | ✓ | ✓ | ✓ | ✓ |
| `getAllUsers should return all registered users` | ✓ | ✓ | ✓ | ✓ |
| `multiple users should have different ids` | ✓ | ✓ | ✓ | ✓ |

## 테스트 코드 차이점 (Setup 부분만 다름)

### Level 1: Manual DI
```kotlin
@BeforeEach
fun setUp() {
    // UserService가 내부에서 의존성을 직접 생성
    userService = UserService()
}
```

**문제점:**
- Mock 주입 불가
- 실제 구현체가 항상 사용됨
- 테스트 격리 어려움

---

### Level 2: Simple Container
```kotlin
@BeforeEach
fun setUp() {
    // DIContainer로 의존성 관리
    container = DIContainer()
    container.register<UserRepository>(UserRepositoryImpl())
    container.register<NotificationService>(EmailNotificationService())

    userService = UserService(
        userRepository = container.get(),
        notificationService = container.get()
    )
}
```

**개선점:**
- Mock 객체로 교체 가능
- 테스트마다 새 컨테이너 생성
- 수동 등록이지만 유연함

---

### Level 3: Reflection DI
```kotlin
@BeforeEach
fun setUp() {
    // ReflectionContainer가 자동으로 Bean 생성 및 주입
    container = ReflectionContainer("com.example.reflection")
    userService = container.getBean()
}
```

**개선점:**
- 자동 컴포넌트 스캔
- @Component, @Inject로 자동 주입
- 수동 등록 코드 불필요

---

### Level 4: Spring Framework
```kotlin
@SpringBootTest
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    // setUp 메서드 불필요 - Spring이 자동 주입
}
```

**개선점:**
- @SpringBootTest로 전체 컨텍스트 로드
- @Autowired로 자동 주입
- @MockBean으로 Mock 교체 가능
- @Transactional로 롤백 지원
- 프로덕션과 동일한 환경

## 테스트 본문 (완전히 동일)

모든 레벨에서 테스트 본문은 100% 동일합니다:

```kotlin
@Test
@DisplayName("사용자를 등록하면 ID가 자동 할당된다")
fun `registerUser should assign id automatically`() {
    // given
    val name = "홍길동"
    val email = "hong@example.com"

    // when
    val user = userService.registerUser(name, email)

    // then
    assertNotNull(user.id)
    assertEquals(name, user.name)
    assertEquals(email, user.email)
}
```

## 실행 결과

```bash
$ cd 02-di-ioc

# Level 1: Manual DI
$ cd 1-manual-di && ./gradlew test
BUILD SUCCESSFUL - 5 tests passed

# Level 2: Simple Container
$ cd 2-simple-container && ./gradlew test
BUILD SUCCESSFUL - 5 tests passed

# Level 3: Reflection DI
$ cd 3-reflection-di && ./gradlew test
BUILD SUCCESSFUL - 5 tests passed

# Level 4: Spring Framework
$ cd 4-spring-di && ./gradlew test
BUILD SUCCESSFUL - 5 tests passed
```

## 결론

**동일한 비즈니스 로직, 동일한 테스트, 다른 DI 구현 방식**

4가지 레벨 모두에서 동일한 테스트가 통과하는 것은 다음을 증명합니다:

1. **기능적 동등성**: 모든 DI 방식이 동일한 결과 제공
2. **점진적 개선**: Level 1 → 4로 갈수록 테스트 편의성 향상
3. **Spring의 가치**: Spring은 단순 편의 기능이 아닌 엔터프라이즈급 솔루션

각 레벨은 다음 레벨의 문제를 해결하면서 발전했고, 최종적으로 Spring이 제공하는 완전한 DI/IoC 컨테이너에 도달합니다.
