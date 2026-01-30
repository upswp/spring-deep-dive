package com.example.reflection.service

import com.example.reflection.annotation.Component
import com.example.reflection.annotation.Inject
import com.example.reflection.model.User
import com.example.reflection.notification.NotificationService
import com.example.reflection.repository.UserRepository

/**
 * 사용자 서비스 - Reflection DI 버전
 *
 * Level 3 개선점:
 * 1. @Component로 자동 등록
 * 2. @Inject로 의존성 자동 주입
 * 3. 의존성 연결 코드 불필요
 * 4. 순수하게 비즈니스 로직에만 집중
 *
 * 여전히 남은 문제:
 * 1. 같은 인터페이스의 구현체가 여러 개면 선택 불가
 * 2. Scope 관리 불가 (모두 싱글톤)
 * 3. Lifecycle 콜백 없음 (초기화/소멸)
 * 4. AOP, 트랜잭션 등 고급 기능 없음
 */
@Component
class UserService(
    @Inject private val userRepository: UserRepository,
    @Inject private val notificationService: NotificationService
) {
    fun registerUser(name: String, email: String): User {
        println("\n=== 사용자 등록 시작 ===")

        val user = User(name = name, email = email)
        val saved = userRepository.save(user)
        notificationService.send("환영합니다, ${saved.name}님!")

        println("=== 사용자 등록 완료 ===\n")
        return saved
    }

    fun getUser(id: Long): User? {
        return userRepository.findById(id)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}
