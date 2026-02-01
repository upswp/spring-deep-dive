package com.example.reflection.service

import com.example.reflection.ReflectionContainer
import com.example.reflection.getBean
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Level 3: Reflection DI 테스트
 *
 * 개선점:
 * - ReflectionContainer가 자동으로 컴포넌트 스캔
 * - @Component, @Inject로 자동 주입
 * - 수동 등록 코드 불필요
 */
class UserServiceTest {
    private lateinit var container: ReflectionContainer
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        // Level 3: ReflectionContainer가 자동으로 Bean 생성 및 주입
        container = ReflectionContainer("com.example.reflection")
        userService = container.getBean()
    }

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
        assertEquals(3, users.size)
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
