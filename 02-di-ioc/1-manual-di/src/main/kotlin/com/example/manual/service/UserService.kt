package com.example.manual.service

import com.example.manual.model.User
import com.example.manual.notification.EmailNotificationService
import com.example.manual.repository.UserRepositoryImpl

/**
 * 사용자 서비스 - Manual DI 버전
 *
 * 문제점:
 * 1. 구체적인 구현체(UserRepositoryImpl, EmailNotificationService)에 직접 의존
 * 2. 구현체를 변경하려면 이 코드를 직접 수정해야 함
 * 3. 테스트 시 Mock 객체를 주입할 수 없음
 * 4. UserService가 객체 생성 책임까지 가지고 있음 (SRP 위반)
 */
class UserService {
    // 문제: 구체적인 구현체를 직접 생성!
    private val userRepository = UserRepositoryImpl()
    private val notificationService = EmailNotificationService()

    fun registerUser(name: String, email: String): User {
        println("\n=== 사용자 등록 시작 ===")

        // 비즈니스 로직
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

/*
 * 만약 SMS로 알림을 변경하려면?
 *
 * 1. 이 파일을 열어서
 * 2. EmailNotificationService를 SmsNotificationService로 변경
 * 3. 재컴파일
 * 4. 테스트 다시 실행
 *
 * 규모가 큰 프로젝트에서는 이런 변경이 수십, 수백 곳에서 필요할 수 있음!
 */
