package com.example.container

import com.example.container.notification.EmailNotificationService
import com.example.container.notification.NotificationService
import com.example.container.notification.SmsNotificationService
import com.example.container.repository.UserRepository
import com.example.container.repository.UserRepositoryImpl
import com.example.container.service.UserService

/**
 * Level 2: Simple DI Container
 *
 * Level 1 대비 개선점:
 * 1. 객체 생성이 중앙화됨
 * 2. 싱글톤 보장
 * 3. Email → SMS 변경이 Main에서만 일어남
 *
 * 여전히 남은 문제:
 * 1. 수동으로 등록해야 함
 * 2. 의존성 자동 주입 불가
 */
fun main() {
    println("=" * 60)
    println("Level 2: Simple DI Container")
    println("=" * 60)
    println()

    // 1. DI 컨테이너 생성
    val container = DIContainer()

    // 2. 의존성 등록 (한 곳에서만!)
    println("=== 의존성 등록 ===")
    container.register<UserRepository>(UserRepositoryImpl())
    container.register<NotificationService>(EmailNotificationService())
    // 주석 해제하면 SMS로 변경됨 (UserService 코드 수정 불필요!)
    // container.register<NotificationService>(SmsNotificationService())

    // 3. 의존성 주입하여 UserService 생성
    val userService = UserService(
        userRepository = container.get(),
        notificationService = container.get()
    )
    container.register(userService)

    // 4. 등록된 Bean 확인
    container.printRegisteredBeans()

    // 5. 사용자 등록
    val user1 = userService.registerUser("홍길동", "hong@example.com")
    val user2 = userService.registerUser("김철수", "kim@example.com")

    // 6. 조회
    println("등록된 사용자: ${userService.getAllUsers().size}명")

    println()
    println("=" * 60)
    println("Level 2 개선점")
    println("=" * 60)
    println("✓ 객체 생성이 Main에서 중앙화됨")
    println("✓ 같은 Repository 인스턴스 재사용 가능")
    println("✓ Email → SMS 변경 시 UserService 수정 불필요")
    println("✓ 인터페이스에 의존하여 테스트 가능성 향상")
    println()
    println("=" * 60)
    println("여전히 남은 문제")
    println("=" * 60)
    println("1. 의존성을 수동으로 등록해야 함")
    println("2. 의존성이 많아지면 등록 코드가 복잡해짐")
    println("3. 타입 안전성 부족 (런타임에 에러 발생 가능)")
    println("4. 의존성 자동 주입 불가")
    println()
    println("→ 해결책: Level 3에서 Reflection 기반 자동 주입 도입")
    println("=" * 60)
}

operator fun String.times(n: Int) = repeat(n)
