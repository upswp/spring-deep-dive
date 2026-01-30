package com.example.container.repository

import com.example.container.model.User

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
 * Level 2 개선점:
 * - 이제 DIContainer에서 관리됨
 * - 싱글톤 보장
 * - 다른 서비스에서도 동일한 인스턴스 재사용 가능
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
