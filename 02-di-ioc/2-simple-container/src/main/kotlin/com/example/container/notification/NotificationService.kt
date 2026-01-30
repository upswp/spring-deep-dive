package com.example.container.notification

/**
 * 알림 서비스 인터페이스
 */
interface NotificationService {
    fun send(message: String)
}

/**
 * 이메일 알림 서비스
 *
 * Level 2 개선점:
 * - Email → SMS 변경이 Main.kt에서만 일어남
 * - UserService 코드 수정 불필요
 */
class EmailNotificationService : NotificationService {
    override fun send(message: String) {
        println("[Email] 전송: $message")
    }
}

/**
 * SMS 알림 서비스
 */
class SmsNotificationService : NotificationService {
    override fun send(message: String) {
        println("[SMS] 전송: $message")
    }
}
