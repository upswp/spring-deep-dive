package com.example.spring.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Level 4: Spring Framework 테스트
 *
 * Spring의 장점:
 * - @SpringBootTest로 전체 ApplicationContext 로드
 * - @Autowired로 자동 주입
 * - @MockBean으로 Mock 교체 가능
 * - @TestConfiguration으로 테스트 전용 설정
 * - 트랜잭션 롤백 지원 (@Transactional)
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Test
    @DisplayName("사용자를 등록하면 ID가 자동 할당된다")
    fun `registerUser should assign id automatically`() {
        // given
        val name = "홍길동"
        val email = "hong@example.com"

        // when
        val user = userService.registerUser(name, email)

        // then
        assertNotNull(user.id)
        assertEquals(name, user.name)
        assertEquals(email, user.email)
    }

    @Test
    @DisplayName("등록된 사용자를 ID로 조회할 수 있다")
    fun `getUser should return user by id`() {
        // given
        val registered = userService.registerUser("김철수", "kim@example.com")

        // when
        val found = userService.getUser(registered.id!!)

        // then
        assertNotNull(found)
        assertEquals(registered.id, found?.id)
        assertEquals(registered.name, found?.name)
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 시 null을 반환한다")
    fun `getUser should return null for non-existent id`() {
        // when
        val user = userService.getUser(999L)

        // then
        assertNull(user)
    }

    @Test
    @DisplayName("전체 사용자 목록을 조회할 수 있다")
    fun `getAllUsers should return all registered users`() {
        // given
        userService.registerUser("홍길동", "hong@example.com")
        userService.registerUser("김철수", "kim@example.com")
        userService.registerUser("이영희", "lee@example.com")

        // when
        val users = userService.getAllUsers()

        // then
        assertTrue(users.size >= 3) // 다른 테스트의 데이터가 포함될 수 있음
    }

    @Test
    @DisplayName("여러 사용자를 등록하면 각각 다른 ID가 할당된다")
    fun `multiple users should have different ids`() {
        // when
        val user1 = userService.registerUser("User1", "user1@example.com")
        val user2 = userService.registerUser("User2", "user2@example.com")
        val user3 = userService.registerUser("User3", "user3@example.com")

        // then
        assertNotEquals(user1.id, user2.id)
        assertNotEquals(user2.id, user3.id)
        assertNotEquals(user1.id, user3.id)
    }
}
