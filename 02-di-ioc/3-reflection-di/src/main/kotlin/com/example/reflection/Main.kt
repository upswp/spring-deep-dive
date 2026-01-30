package com.example.reflection

import com.example.reflection.service.UserService

/**
 * Level 3: Reflection 기반 DI
 *
 * Level 2 대비 개선점:
 * 1. @Component로 자동 등록
 * 2. @Inject로 의존성 자동 주입
 * 3. 패키지 스캔으로 클래스 자동 발견
 *
 * 여전히 남은 문제:
 * 1. Qualifier 부족 (같은 타입 여러 개 선택 불가)
 * 2. Scope 관리 불가
 * 3. Lifecycle 콜백 없음
 * 4. AOP, 트랜잭션 등 고급 기능 없음
 */
fun main() {
    println("=" * 60)
    println("Level 3: Reflection 기반 DI")
    println("=" * 60)
    println()

    // 1. 컨테이너 생성 (패키지 스캔 자동 실행)
    val container = ReflectionContainer("com.example.reflection")

    // 2. Bean 확인
    container.printBeans()

    // 3. UserService 가져오기 (이미 모든 의존성이 주입되어 있음!)
    val userService = container.getBean<UserService>()

    // 4. 사용자 등록
    val user1 = userService.registerUser("홍길동", "hong@example.com")
    val user2 = userService.registerUser("김철수", "kim@example.com")

    // 5. 조회
    println("등록된 사용자: ${userService.getAllUsers().size}명")

    println()
    println("=" * 60)
    println("Level 3 개선점")
    println("=" * 60)
    println("✓ @Component로 자동 등록")
    println("✓ @Inject로 의존성 자동 주입")
    println("✓ 패키지 스캔으로 클래스 자동 발견")
    println("✓ 수동 등록/주입 코드 완전 제거")
    println("✓ 의존성 순서 자동 해결")
    println()
    println("=" * 60)
    println("여전히 남은 문제")
    println("=" * 60)
    println("1. Qualifier 부족 (같은 타입의 Bean이 여러 개면?)")
    println("2. Scope 관리 불가 (모두 싱글톤)")
    println("3. Lifecycle 콜백 없음 (@PostConstruct, @PreDestroy)")
    println("4. 순환 의존성 감지 불가")
    println("5. AOP, 트랜잭션, 보안 등 고급 기능 없음")
    println()
    println("→ 해결책: Level 4에서 Spring Framework 사용")
    println("=" * 60)
}

operator fun String.times(n: Int) = repeat(n)
