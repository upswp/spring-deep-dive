package com.example.withaop.service;

import com.example.withaop.model.User;
import com.example.withaop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetUser() {
        // Given: 사용자가 존재함
        Long userId = 1L;

        // When: 사용자를 조회하면
        User user = userService.getUser(userId);

        // Then: 사용자 정보가 반환됨
        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("John Doe", user.getName());

        // And: AOP가 자동으로 적용되어 로깅, 성능측정이 수행됨
        // (콘솔 출력으로 확인 가능)
    }

    @Test
    void testCreateUser() {
        // Given: 새 사용자 정보
        String name = "Bob Smith";
        String email = "bob@example.com";

        // When: 사용자를 생성하면
        User newUser = userService.createUser(name, email);

        // Then: 사용자가 생성되고 ID가 부여됨
        assertNotNull(newUser);
        assertNotNull(newUser.getId());
        assertEquals(name, newUser.getName());
        assertEquals(email, newUser.getEmail());

        // And: 실제로 저장되었는지 확인
        User savedUser = userRepository.findById(newUser.getId());
        assertEquals(name, savedUser.getName());

        // And: AOP가 자동으로 적용됨 (비즈니스 로직에는 AOP 코드 없음!)
    }

    @Test
    void testDeleteUser() {
        // Given: 사용자가 존재함
        User user = userService.createUser("Test User", "test@example.com");
        Long userId = user.getId();

        // When: 사용자를 삭제하면
        userService.deleteUser(userId);

        // Then: 사용자가 삭제됨
        assertThrows(RuntimeException.class, () -> {
            userRepository.findById(userId);
        });
    }

    @Test
    void testGetUserNotFound() {
        // Given: 존재하지 않는 사용자 ID
        Long nonExistentId = 999L;

        // When & Then: 예외가 발생함
        assertThrows(RuntimeException.class, () -> {
            userService.getUser(nonExistentId);
        });

        // And: ExceptionAspect가 예외를 자동으로 로깅함
    }

    @Test
    void testAOPIsApplied() {
        // Given: AOP가 활성화되어 있음

        // When: 메서드를 호출하면
        User user = userService.getUser(1L);

        // Then: 비즈니스 로직만 있는 깔끔한 코드로 모든 기능 수행
        // - 로깅 (LoggingAspect)
        // - 성능 측정 (PerformanceAspect)
        // - 보안 체크 (SecurityAspect)
        // - 예외 처리 (ExceptionAspect)

        assertNotNull(user);
        // 콘솔 출력으로 AOP가 작동하는 것을 확인할 수 있음
    }
}

/*
 * 테스트 실행 시 콘솔 출력:
 *
 * ========================================
 * 메서드 호출: getUser
 * 파라미터: [1]
 *   → 사용자 조회: John Doe
 * 실행 시간: 1ms
 * 반환값: User{id=1, name='John Doe', email='john@example.com'}
 * ========================================
 *
 * → AOP가 자동으로 작동하는 것을 확인!
 */
