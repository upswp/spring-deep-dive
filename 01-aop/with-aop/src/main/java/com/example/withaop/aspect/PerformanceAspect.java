package com.example.withaop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 성능 측정 Aspect
 *
 * 모든 서비스 메서드의 실행 시간을 자동으로 측정합니다.
 */
@Aspect
@Component
public class PerformanceAspect {

    /**
     * @Around: 메서드 실행 전후에 코드 실행
     * execution(* com.example.withaop.service.*.*(..)): 모든 서비스 메서드에 적용
     */
    @Around("execution(* com.example.withaop.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 실행 시간 측정 시작
        long startTime = System.currentTimeMillis();

        // 실제 메서드 실행
        Object result = joinPoint.proceed();

        // 실행 시간 측정 종료
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("실행 시간: " + executionTime + "ms");

        return result;
    }
}

/*
 * 이점:
 * 1. 모든 서비스 메서드에 자동 적용
 * 2. 새 메서드 추가 시 추가 코드 불필요
 * 3. 포인트컷만 변경하면 다른 패키지에도 적용 가능
 */
