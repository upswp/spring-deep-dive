package com.example.container

/**
 * Level 2: 간단한 DI 컨테이너
 *
 * 장점:
 * 1. 객체 생성을 중앙화
 * 2. 싱글톤 보장 (같은 인스턴스 재사용)
 * 3. 의존성 변경이 한 곳에서만 일어남
 *
 * 한계:
 * 1. 여전히 수동으로 등록해야 함
 * 2. 타입 안전성 부족
 * 3. 의존성 자동 주입 불가
 */
class DIContainer {
    private val beans = mutableMapOf<Class<*>, Any>()

    /**
     * 의존성 등록
     * 같은 타입은 하나만 등록 가능 (Last-Win)
     */
    fun <T : Any> register(type: Class<T>, instance: T) {
        beans[type] = instance
        println("[Container] ${type.simpleName} 등록 완료")
    }

    /**
     * 의존성 조회
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: Class<T>): T {
        return beans[type] as? T
            ?: throw IllegalStateException("${type.simpleName}이(가) 컨테이너에 등록되지 않았습니다")
    }

    /**
     * 등록된 Bean 목록 출력
     */
    fun printRegisteredBeans() {
        println("\n[Container] 등록된 Bean 목록:")
        beans.keys.forEach {
            println("  - ${it.simpleName}")
        }
        println()
    }
}

/**
 * Kotlin DSL 스타일로 사용하기 위한 확장 함수
 */
inline fun <reified T : Any> DIContainer.register(instance: T) {
    register(T::class.java, instance)
}

inline fun <reified T : Any> DIContainer.get(): T {
    return get(T::class.java)
}
