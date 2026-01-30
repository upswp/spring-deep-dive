# 🔍 AOP 사용 전후 비교

> AOP를 사용하지 않았을 때와 사용했을 때의 코드를 직접 비교합니다.

---

## 📋 시나리오

**간단한 사용자 관리 서비스**를 구현합니다.

### 기능
- 사용자 조회 (`getUser`)
- 사용자 생성 (`createUser`)
- 사용자 삭제 (`deleteUser`)

### 공통 관심사 (Cross-Cutting Concerns)
1. **성능 측정**: 메서드 실행 시간 로깅
2. **메서드 로깅**: 입력 파라미터 및 반환값 로깅
3. **예외 처리**: 예외 발생 시 상세 로깅
4. **보안 체크**: 관리자 권한 확인

---

## ❌ AOP 없이 구현 (without-aop/)

### 문제점

모든 공통 관심사를 **각 메서드마다 직접 구현**해야 합니다.

```java
@Service
public class UserService {

    public User getUser(Long userId) {
        // 1. 실행 시간 측정 시작
        long startTime = System.currentTimeMillis();

        // 2. 메서드 로깅
        System.out.println("=== getUser 호출 ===");
        System.out.println("파라미터: userId=" + userId);

        try {
            // 3. 보안 체크
            if (!SecurityContext.isAdmin()) {
                throw new SecurityException("권한이 없습니다.");
            }

            // 실제 비즈니스 로직 (단 2줄!)
            User user = userRepository.findById(userId);
            System.out.println("사용자 조회: " + user.getName());

            // 4. 반환값 로깅
            System.out.println("반환값: " + user);

            // 5. 실행 시간 측정 종료
            long endTime = System.currentTimeMillis();
            System.out.println("실행 시간: " + (endTime - startTime) + "ms");

            return user;

        } catch (Exception e) {
            // 6. 예외 처리
            System.err.println("예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public User createUser(String name, String email) {
        // 위와 동일한 코드 반복... (25줄)
        long startTime = System.currentTimeMillis();
        System.out.println("=== createUser 호출 ===");
        // ... 중복 코드 ...
    }

    public void deleteUser(Long userId) {
        // 또 동일한 코드 반복... (25줄)
        long startTime = System.currentTimeMillis();
        System.out.println("=== deleteUser 호출 ===");
        // ... 중복 코드 ...
    }
}
```

### 결과

- ❌ **코드 중복**: 각 메서드마다 25줄 이상의 중복 코드
- ❌ **가독성 저하**: 비즈니스 로직이 공통 관심사에 묻혀버림
- ❌ **유지보수 어려움**: 로깅 형식 변경 시 모든 메서드 수정 필요
- ❌ **실수 가능성**: 새 메서드 추가 시 공통 코드 누락 가능

**비즈니스 로직 2줄을 위해 25줄의 중복 코드 작성!**

---

## ✅ AOP 사용 (with-aop/)

### 해결 방법

공통 관심사를 **Aspect로 분리**합니다.

### 1. 깔끔한 비즈니스 로직

```java
@Service
public class UserService {

    public User getUser(Long userId) {
        // 순수 비즈니스 로직만!
        User user = userRepository.findById(userId);
        System.out.println("사용자 조회: " + user.getName());
        return user;
    }

    public User createUser(String name, String email) {
        // 순수 비즈니스 로직만!
        User user = new User(name, email);
        userRepository.save(user);
        System.out.println("사용자 생성: " + user.getName());
        return user;
    }

    public void deleteUser(Long userId) {
        // 순수 비즈니스 로직만!
        userRepository.deleteById(userId);
        System.out.println("사용자 삭제: " + userId);
    }
}
```

### 2. 공통 관심사는 Aspect로

```java
// 성능 측정
@Aspect
@Component
public class PerformanceAspect {

    @Around("execution(* com.example.withaop.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        System.out.println(joinPoint.getSignature().getName() +
                          " 실행 시간: " + (endTime - startTime) + "ms");
        return result;
    }
}

// 메서드 로깅
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.withaop.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("=== " + joinPoint.getSignature().getName() + " 호출 ===");
        System.out.println("파라미터: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(
        pointcut = "execution(* com.example.withaop.service.*.*(..))",
        returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("반환값: " + result);
    }
}

// 예외 처리
@Aspect
@Component
public class ExceptionAspect {

    @AfterThrowing(
        pointcut = "execution(* com.example.withaop.service.*.*(..))",
        throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {
        System.err.println("예외 발생: " + joinPoint.getSignature().getName());
        System.err.println("예외 메시지: " + ex.getMessage());
        ex.printStackTrace();
    }
}

// 보안 체크
@Aspect
@Component
public class SecurityAspect {

    @Before("execution(* com.example.withaop.service.*.*(..))")
    public void checkSecurity(JoinPoint joinPoint) {
        if (!SecurityContext.isAdmin()) {
            throw new SecurityException("권한이 없습니다.");
        }
    }
}
```

### 결과

- ✅ **코드 중복 제거**: 공통 관심사를 한 곳에서 관리
- ✅ **가독성 향상**: 비즈니스 로직이 명확히 보임
- ✅ **유지보수 용이**: 로깅 형식 변경 시 Aspect만 수정
- ✅ **일관성 보장**: 새 메서드 추가 시 자동으로 공통 관심사 적용

**비즈니스 로직 3줄 + Aspect 분리로 깔끔한 코드!**

---

## 📊 정량적 비교

### 코드 라인 수

| 항목 | Without AOP | With AOP | 개선율 |
|------|-------------|----------|--------|
| UserService | 75줄 | 15줄 | **80% 감소** |
| 공통 관심사 | 60줄 (중복) | 40줄 (분리) | **중복 제거** |
| 총 코드 | 75줄 | 55줄 | **27% 감소** |

### 유지보수성

| 시나리오 | Without AOP | With AOP |
|---------|-------------|----------|
| 로깅 형식 변경 | 3개 메서드 수정 | 1개 Aspect 수정 |
| 새 메서드 추가 | 25줄 공통 코드 복사 | 3줄만 작성 |
| 성능 측정 비활성화 | 3개 메서드 수정 | 1개 Aspect 주석 처리 |
| 새 공통 관심사 추가 | 모든 메서드 수정 | 새 Aspect 추가만 |

---

## 🎯 핵심 이점

### 1. 관심사의 분리 (Separation of Concerns)

**Before AOP:**
```
비즈니스 로직 + 로깅 + 성능측정 + 예외처리 + 보안
= 한 메서드에 모든 것이 섞여있음
```

**After AOP:**
```
비즈니스 로직 → UserService
로깅 → LoggingAspect
성능측정 → PerformanceAspect
예외처리 → ExceptionAspect
보안 → SecurityAspect
= 각자 책임이 명확히 분리됨
```

### 2. 재사용성

```java
// 포인트컷만 변경하면 다른 서비스에도 즉시 적용!
@Around("execution(* com.example.withaop.service.*.*(..))")  // 모든 서비스
@Around("execution(* com.example.withaop.repository.*.*(..))")  // 모든 리포지토리
```

### 3. 테스트 용이성

```java
// AOP 없이 비즈니스 로직만 테스트 가능
@Test
void testGetUser() {
    User user = userService.getUser(1L);
    assertNotNull(user);
    // 로깅, 성능측정 등에 방해받지 않음
}
```

---

## 🏃 실행 및 비교

### Without AOP 실행

```bash
cd without-aop
./gradlew bootRun
```

**출력:**
```
=== getUser 호출 ===
파라미터: userId=1
사용자 조회: John Doe
반환값: User{id=1, name='John Doe'}
실행 시간: 45ms
```

### With AOP 실행

```bash
cd with-aop
./gradlew bootRun
```

**출력:**
```
=== getUser 호출 ===
파라미터: [1]
사용자 조회: John Doe
반환값: User{id=1, name='John Doe'}
getUser 실행 시간: 45ms
```

**결과는 동일하지만, 코드는 훨씬 깔끔!**

---

## 💡 실무 활용 사례

### 1. 트랜잭션 관리
```java
// AOP 없이
public void transferMoney() {
    try {
        transactionManager.begin();
        // 비즈니스 로직
        transactionManager.commit();
    } catch (Exception e) {
        transactionManager.rollback();
    }
}

// AOP 사용
@Transactional
public void transferMoney() {
    // 비즈니스 로직만!
}
```

### 2. 캐싱
```java
// AOP 사용
@Cacheable("users")
public User getUser(Long id) {
    return userRepository.findById(id);
}
// 자동으로 캐시 확인 → 없으면 DB 조회 → 캐시 저장
```

### 3. 보안
```java
// AOP 사용
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) {
    userRepository.deleteById(id);
}
// 자동으로 권한 체크
```

---

## 📚 더 알아보기

- [README.md](./README.md) - AOP 상세 개념
- `without-aop/` - AOP 없는 전체 코드
- `with-aop/` - AOP 사용 전체 코드

---

**결론: AOP는 코드를 깔끔하게 하고, 유지보수를 쉽게 만듭니다!**
