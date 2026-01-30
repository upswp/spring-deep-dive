package com.example.withaop.service;

import com.example.withaop.model.User;
import com.example.withaop.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * AOP를 사용한 깔끔한 UserService
 *
 * 장점:
 * 1. 순수한 비즈니스 로직만 포함
 * 2. 공통 관심사는 Aspect로 분리
 * 3. 가독성 향상, 유지보수 용이
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 조회
     *
     * 공통 관심사(성능측정, 로깅, 예외처리, 보안)는
     * Aspect에서 자동으로 처리됩니다!
     */
    public User getUser(Long userId) {
        // 순수한 비즈니스 로직만!
        User user = userRepository.findById(userId);
        System.out.println("  → 사용자 조회: " + user.getName());
        return user;
    }

    /**
     * 사용자 생성
     */
    public User createUser(String name, String email) {
        // 순수한 비즈니스 로직만!
        User user = new User(null, name, email);
        userRepository.save(user);
        System.out.println("  → 사용자 생성: " + user.getName());
        return user;
    }

    /**
     * 사용자 삭제
     */
    public void deleteUser(Long userId) {
        // 순수한 비즈니스 로직만!
        userRepository.deleteById(userId);
        System.out.println("  → 사용자 삭제: " + userId);
    }
}

/*
 * 비교:
 * - Without AOP: 각 메서드 30줄 (비즈니스 로직 2줄 + 공통 관심사 28줄)
 * - With AOP: 각 메서드 5줄 (비즈니스 로직만!)
 *
 * 코드 감소: 83% 감소!
 * 가독성: 월등히 향상!
 * 유지보수: 훨씬 쉬움!
 */
