package com.example.container.service

import com.example.container.model.User
import com.example.container.notification.NotificationService
import com.example.container.repository.UserRepository

/**
 * 사용자 서비스 - Simple Container 버전
 *
 * Level 2 개선점:
 * 1. 생성자 주입(Constructor Injection) 사용
 * 2. 인터페이스에 의존 (구현체에 의존 X)
 * 3. UserService는 비즈니스 로직에만 집중
 * 4. 의존성 변경이 UserService 코드 수정 없이 가능
 *
 * 여전히 남은 문제:
 * 1. Main에서 수동으로 의존성을 연결해야 함
 * 2. 의존성이 많아지면 등록 코드가 복잡해짐
 */
class UserService(
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
) {
    fun registerUser(name: String, email: String): User {
        println("\n=== 사용자 등록 시작 ===")

        // 비즈니스 로직만 집중
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
