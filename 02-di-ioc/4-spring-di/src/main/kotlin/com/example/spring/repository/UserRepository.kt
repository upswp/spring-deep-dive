package com.example.spring.repository

import com.example.spring.model.User
import org.springframework.stereotype.Repository

/**
 * 사용자 저장소 인터페이스
 */
interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findAll(): List<User>
}

/**
 * 사용자 저장소 구현체
 *
 * Level 4 (Spring):
 * - @Repository로 자동 등록
 * - Spring이 자동으로 Bean 관리
 * - 예외 변환 (DataAccessException) 자동 제공
 * - 트랜잭션 지원
 */
@Repository
class UserRepositoryImpl : UserRepository {
    private val users = mutableMapOf<Long, User>()
    private var sequence = 0L

    override fun save(user: User): User {
        val id = user.id ?: ++sequence
        val saved = user.copy(id = id)
        users[id] = saved
        println("[Repository] 사용자 저장: ${saved.name} (ID: $id)")
        return saved
    }

    override fun findById(id: Long): User? {
        return users[id]
    }

    override fun findAll(): List<User> {
        return users.values.toList()
    }
}
