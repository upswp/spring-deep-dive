package com.example.manual.repository

import com.example.manual.model.User

/**
 * 사용자 저장소 인터페이스
 */
interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findAll(): List<User>
}

/**
 * 사용자 저장소 구현체 (메모리 기반)
 *
 * 문제점: UserService가 이 구체적인 구현체를 직접 생성해야 함
 */
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
