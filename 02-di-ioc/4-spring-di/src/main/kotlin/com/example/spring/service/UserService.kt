package com.example.spring.service

import com.example.spring.model.User
import com.example.spring.notification.NotificationService
import com.example.spring.repository.UserRepository
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Service

/**
 * 사용자 서비스 - Spring 버전
 *
 * Level 4 (Spring) 장점:
 * 1. @Service로 자동 등록 및 트랜잭션 지원
 * 2. Constructor Injection 자동 지원 (Kotlin은 @Autowired 불필요)
 * 3. @PostConstruct/@PreDestroy로 Lifecycle 관리
 * 4. @Qualifier로 같은 타입 Bean 선택 가능
 * 5. @Scope로 Bean 범위 설정 가능
 * 6. AOP로 횡단 관심사 처리
 * 7. 프로파일별 설정 (@Profile)
 * 8. 테스트 지원 (@MockBean, @SpyBean)
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
    // Kotlin에서는 생성자 주입이 기본이므로 @Autowired 불필요
) {
    @PostConstruct
    fun init() {
        println("[UserService] 초기화됨 (@PostConstruct)")
    }

    @PreDestroy
    fun destroy() {
        println("[UserService] 종료됨 (@PreDestroy)")
    }

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
