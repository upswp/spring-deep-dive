package com.example.withaop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 예외 처리 Aspect
 *
 * 모든 서비스 메서드에서 발생하는 예외를 자동으로 처리하고 로깅합니다.
 */
@Aspect
@Component
public class ExceptionAspect {

    /**
     * @AfterThrowing: 메서드 실행 중 예외 발생 시 코드 실행
     * throwing: 예외 객체를 파라미터로 받음
     */
    @AfterThrowing(
        pointcut = "execution(* com.example.withaop.service.*.*(..))",
        throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {
        System.err.println("========================================");
        System.err.println("예외 발생 메서드: " + joinPoint.getSignature().getName());
        System.err.println("예외 메시지: " + ex.getMessage());
        System.err.println("========================================\n");
        ex.printStackTrace();

        // 추가로 할 수 있는 일:
        // - 관리자에게 이메일/슬랙 알림 전송
        // - 에러 로그를 외부 모니터링 시스템에 전송
        // - 특정 예외에 대한 복구 로직 실행
    }
}

/*
 * 이점:
 * 1. 모든 예외를 일관되게 처리
 * 2. 예외 처리 로직 변경 시 이 파일만 수정
 * 3. 예외 발생 시 추가 작업(알림 등)을 쉽게 추가 가능
 */
