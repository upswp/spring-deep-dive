package com.example.withaop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 로깅 Aspect
 *
 * 메서드 호출 시 파라미터와 반환값을 자동으로 로깅합니다.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * @Before: 메서드 실행 전에 코드 실행
     */
    @Before("execution(* com.example.withaop.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("\n========================================");
        System.out.println("메서드 호출: " + joinPoint.getSignature().getName());
        System.out.println("파라미터: " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * @AfterReturning: 메서드 정상 종료 후 코드 실행
     * returning: 반환값을 파라미터로 받음
     */
    @AfterReturning(
        pointcut = "execution(* com.example.withaop.service.*.*(..))",
        returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("반환값: " + result);
    }
}

/*
 * 이점:
 * 1. 모든 메서드의 입출력을 일관되게 로깅
 * 2. 로깅 형식 변경 시 이 파일만 수정
 * 3. 디버깅에 매우 유용
 */
