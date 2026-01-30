package com.example.without.service;

import com.example.without.model.User;
import com.example.without.repository.UserRepository;
import com.example.without.util.SecurityContext;
import org.springframework.stereotype.Service;

/**
 * AOP 없이 구현한 UserService
 *
 * 문제점:
 * 1. 모든 메서드에 중복 코드 (성능측정, 로깅, 예외처리, 보안체크)
 * 2. 비즈니스 로직이 공통 관심사에 묻혀버림
 * 3. 유지보수 어려움 (로깅 형식 변경 시 모든 메서드 수정 필요)
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long userId) {
        // ========== 공통 관심사 시작 ==========

        // 1. 실행 시간 측정 시작
        long startTime = System.currentTimeMillis();

        // 2. 메서드 로깅
        System.out.println("\n========================================");
        System.out.println("메서드 호출: getUser");
        System.out.println("파라미터: userId=" + userId);

        try {
            // 3. 보안 체크
            if (!SecurityContext.isAdmin()) {
                throw new SecurityException("권한이 없습니다.");
            }

            // ========== 실제 비즈니스 로직 (단 2줄!) ==========
            User user = userRepository.findById(userId);
            System.out.println("  → 사용자 조회: " + user.getName());
            // ==============================================

            // 4. 반환값 로깅
            System.out.println("반환값: " + user);

            // 5. 실행 시간 측정 종료
            long endTime = System.currentTimeMillis();
            System.out.println("실행 시간: " + (endTime - startTime) + "ms");
            System.out.println("========================================\n");

            return user;

        } catch (Exception e) {
            // 6. 예외 처리 및 로깅
            System.err.println("========================================");
            System.err.println("예외 발생 메서드: getUser");
            System.err.println("예외 메시지: " + e.getMessage());
            System.err.println("========================================\n");
            e.printStackTrace();
            throw e;
        }

        // ========== 공통 관심사 종료 ==========
    }

    public User createUser(String name, String email) {
        // ========== 공통 관심사 시작 (중복!) ==========

        // 1. 실행 시간 측정 시작
        long startTime = System.currentTimeMillis();

        // 2. 메서드 로깅
        System.out.println("\n========================================");
        System.out.println("메서드 호출: createUser");
        System.out.println("파라미터: name=" + name + ", email=" + email);

        try {
            // 3. 보안 체크
            if (!SecurityContext.isAdmin()) {
                throw new SecurityException("권한이 없습니다.");
            }

            // ========== 실제 비즈니스 로직 (단 3줄!) ==========
            User user = new User(null, name, email);
            userRepository.save(user);
            System.out.println("  → 사용자 생성: " + user.getName());
            // ==============================================

            // 4. 반환값 로깅
            System.out.println("반환값: " + user);

            // 5. 실행 시간 측정 종료
            long endTime = System.currentTimeMillis();
            System.out.println("실행 시간: " + (endTime - startTime) + "ms");
            System.out.println("========================================\n");

            return user;

        } catch (Exception e) {
            // 6. 예외 처리 및 로깅
            System.err.println("========================================");
            System.err.println("예외 발생 메서드: createUser");
            System.err.println("예외 메시지: " + e.getMessage());
            System.err.println("========================================\n");
            e.printStackTrace();
            throw e;
        }

        // ========== 공통 관심사 종료 ==========
    }

    public void deleteUser(Long userId) {
        // ========== 공통 관심사 시작 (또 중복!) ==========

        // 1. 실행 시간 측정 시작
        long startTime = System.currentTimeMillis();

        // 2. 메서드 로깅
        System.out.println("\n========================================");
        System.out.println("메서드 호출: deleteUser");
        System.out.println("파라미터: userId=" + userId);

        try {
            // 3. 보안 체크
            if (!SecurityContext.isAdmin()) {
                throw new SecurityException("권한이 없습니다.");
            }

            // ========== 실제 비즈니스 로직 (단 2줄!) ==========
            userRepository.deleteById(userId);
            System.out.println("  → 사용자 삭제: " + userId);
            // ==============================================

            // 4. 반환값 로깅
            System.out.println("반환값: void");

            // 5. 실행 시간 측정 종료
            long endTime = System.currentTimeMillis();
            System.out.println("실행 시간: " + (endTime - startTime) + "ms");
            System.out.println("========================================\n");

        } catch (Exception e) {
            // 6. 예외 처리 및 로깅
            System.err.println("========================================");
            System.err.println("예외 발생 메서드: deleteUser");
            System.err.println("예외 메시지: " + e.getMessage());
            System.err.println("========================================\n");
            e.printStackTrace();
            throw e;
        }

        // ========== 공통 관심사 종료 ==========
    }
}

/*
 * 문제점 요약:
 *
 * 1. 코드 중복: 각 메서드마다 25줄 이상의 동일한 코드 반복
 * 2. 가독성: 비즈니스 로직 2-3줄이 25줄의 공통 관심사에 묻혀버림
 * 3. 유지보수: 로깅 형식을 변경하려면 3개 메서드 모두 수정 필요
 * 4. 확장성: 새 메서드 추가 시 25줄 복사-붙여넣기 필수
 * 5. 실수 가능성: 공통 코드 누락 시 일관성 깨짐
 *
 * 해결: AOP 사용! (with-aop 프로젝트 참고)
 */
