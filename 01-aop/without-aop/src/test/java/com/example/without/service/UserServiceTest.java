package com.example.without.service;

import com.example.without.model.User;
import com.example.without.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
    }
}
