package com.example.spring.notification

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

/**
 * 알림 서비스 인터페이스
 */
interface NotificationService {
    fun send(message: String)
}

/**
 * 이메일 알림 서비스
 *
 * Level 4 (Spring):
 * - @Component로 자동 등록
 * - @Primary로 우선순위 지정 (같은 타입이 여러 개일 때)
 * - Spring이 자동으로 주입
 */
@Primary
@Component
class EmailNotificationService : NotificationService {
    override fun send(message: String) {
        println("[Email] 전송: $message")
    }
}

/**
 * SMS 알림 서비스
 *
 * @Primary를 EmailNotificationService로 옮기면 SMS로 전환됨
 * 또는 @Qualifier("smsNotificationService")로 명시적 선택 가능
 */
@Component
class SmsNotificationService : NotificationService {
    override fun send(message: String) {
        println("[SMS] 전송: $message")
    }
}
