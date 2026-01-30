package com.example.reflection.repository

import com.example.reflection.annotation.Component
import com.example.reflection.model.User

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
 * Level 3 개선점:
 * - @Component만 붙이면 자동으로 컨테이너에 등록됨
 * - 수동 등록 코드 불필요
 */
@Component
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
