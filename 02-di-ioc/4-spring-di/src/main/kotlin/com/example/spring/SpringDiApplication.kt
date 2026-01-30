package com.example.spring

import com.example.spring.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

/**
 * Level 4: Spring Framework
 *
 * Spring이 제공하는 완전한 DI 기능:
 * 1. 자동 컴포넌트 스캔 (@ComponentScan)
 * 2. 의존성 자동 주입 (@Autowired, Constructor Injection)
 * 3. Bean Lifecycle 관리 (@PostConstruct, @PreDestroy)
 * 4. Scope 관리 (@Scope - Singleton, Prototype, Request, Session)
 * 5. Qualifier로 같은 타입 Bean 선택 (@Qualifier, @Primary)
 * 6. 프로파일 (@Profile - dev, prod, test)
 * 7. AOP 지원 (@Aspect, @Transactional)
 * 8. 설정 외부화 (@Value, @ConfigurationProperties)
 * 9. 이벤트 발행/구독 (@EventListener)
 * 10. 조건부 Bean 등록 (@Conditional)
 */
@SpringBootApplication
class SpringDiApplication {

    @Bean
    fun demo(userService: UserService) = CommandLineRunner {
        println("=" * 60)
        println("Level 4: Spring Framework")
        println("=" * 60)
        println()

        println("=== Spring IoC Container 시작 ===")
        println("✓ 모든 Bean 자동 스캔 및 등록")
        println("✓ 의존성 자동 주입 완료")
        println("✓ Lifecycle 콜백 실행 (@PostConstruct)")
        println()

        // 사용자 등록
        val user1 = userService.registerUser("홍길동", "hong@example.com")
        val user2 = userService.registerUser("김철수", "kim@example.com")

        // 조회
        println("등록된 사용자: ${userService.getAllUsers().size}명")

        println()
        println("=" * 60)
        println("Spring의 추가 기능")
        println("=" * 60)
        println("✓ @Transactional: 선언적 트랜잭션 관리")
        println("✓ @Cacheable: 메서드 결과 캐싱")
        println("✓ @Async: 비동기 메서드 실행")
        println("✓ @Scheduled: 스케줄링")
        println("✓ @EventListener: 이벤트 기반 아키텍처")
        println("✓ @Validated: 입력 검증")
        println("✓ @Profile: 환경별 Bean 분리")
        println("✓ @Conditional: 조건부 Bean 등록")
        println("✓ AOP: 횡단 관심사 분리")
        println("✓ 테스트 지원: @MockBean, @SpyBean, @TestConfiguration")
        println()
        println("=" * 60)
        println("Level 1 → Level 4 비교")
        println("=" * 60)
        println("Level 1: UserService가 직접 생성 (강한 결합)")
        println("Level 2: DI 컨테이너로 중앙 관리 (수동 등록)")
        println("Level 3: Reflection으로 자동 주입 (기본 기능)")
        println("Level 4: Spring 완전한 기능 (프로덕션 ready)")
        println("=" * 60)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringDiApplication>(*args)
}

operator fun String.times(n: Int) = repeat(n)
