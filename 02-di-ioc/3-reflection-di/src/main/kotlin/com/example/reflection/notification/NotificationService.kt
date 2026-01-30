package com.example.reflection.notification

import com.example.reflection.annotation.Component

/**
 * 알림 서비스 인터페이스
 */
interface NotificationService {
    fun send(message: String)
}

/**
 * 이메일 알림 서비스
 *
 * Level 3 개선점:
 * - @Component로 자동 등록
 * - Email/SMS 전환은 @Component 위치 변경만으로 가능
 */
@Component
class EmailNotificationService : NotificationService {
    override fun send(message: String) {
        println("[Email] 전송: $message")
    }
}

/**
 * SMS 알림 서비스
 *
 * 주석 해제하면 Email 대신 SMS가 사용됨
 * (같은 인터페이스의 구현체가 여러 개면 마지막 것이 사용됨)
 */
// @Component
class SmsNotificationService : NotificationService {
    override fun send(message: String) {
        println("[SMS] 전송: $message")
    }
}
