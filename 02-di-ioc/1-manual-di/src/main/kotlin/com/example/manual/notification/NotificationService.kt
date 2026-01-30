package com.example.manual.notification

/**
 * 알림 서비스 인터페이스
 */
interface NotificationService {
    fun send(message: String)
}

/**
 * 이메일 알림 서비스
 *
 * 문제점: SMS로 변경하려면 UserService 코드를 직접 수정해야 함
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
