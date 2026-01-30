package com.example.manual

import com.example.manual.service.UserService

/**
 * Level 1: Manual DI (수동 의존성 주입)
 *
 * 이 방식의 문제점을 직접 체험합니다.
 */
fun main() {
    println("=" * 60)
    println("Level 1: Manual DI - 강한 결합")
    println("=" * 60)
    println()

    // UserService가 모든 의존성을 내부에서 직접 생성
    val userService = UserService()

    // 사용자 등록
    val user1 = userService.registerUser("홍길동", "hong@example.com")
    val user2 = userService.registerUser("김철수", "kim@example.com")

    // 조회
    println("등록된 사용자: ${userService.getAllUsers().size}명")

    println()
    println("=" * 60)
    println("문제점 요약")
    println("=" * 60)
    println("1. UserService가 구체적인 구현체에 직접 의존")
    println("2. Email → SMS 변경하려면 UserService.kt 파일 수정 필요")
    println("3. 테스트 시 Mock 객체 주입 불가능")
    println("4. 객체 생성 책임이 UserService에 있음 (SRP 위반)")
    println("5. 다른 곳에서 동일한 Repository 인스턴스 재사용 불가")
    println()
    println("→ 해결책: Level 2에서 DI 컨테이너 도입")
    println("=" * 60)
}

operator fun String.times(n: Int) = repeat(n)
