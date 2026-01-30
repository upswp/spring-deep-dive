package com.example.withaop.aspect;

import com.example.withaop.util.SecurityContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 보안 체크 Aspect
 *
 * 모든 서비스 메서드 실행 전에 권한을 자동으로 확인합니다.
 */
@Aspect
@Component
public class SecurityAspect {

    /**
     * @Before: 메서드 실행 전에 보안 체크
     */
    @Before("execution(* com.example.withaop.service.*.*(..))")
    public void checkSecurity(JoinPoint joinPoint) {
        if (!SecurityContext.isAdmin()) {
            throw new SecurityException("권한이 없습니다. 관리자만 접근 가능합니다.");
        }
        // 권한이 있으면 계속 진행
    }
}

/*
 * 이점:
 * 1. 모든 메서드에 자동으로 보안 체크 적용
 * 2. 보안 로직 변경 시 이 파일만 수정
 * 3. 새 메서드 추가 시 보안 체크 누락 방지
 * 4. 포인트컷을 수정하여 특정 메서드만 보안 체크 가능
 */
