package com.example.withaop;

import com.example.withaop.model.User;
import com.example.withaop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy  // AOP 활성화 (Spring Boot는 자동이지만 명시적으로 표시)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService) {
        return args -> {
            System.out.println("\n");
            System.out.println("=".repeat(60));
            System.out.println("WITH AOP - 깔끔한 코드!");
            System.out.println("=".repeat(60));
            System.out.println();

            // 1. 사용자 조회
            User user = userService.getUser(1L);
            System.out.println("========================================\n");

            // 2. 사용자 생성
            User newUser = userService.createUser("Alice Johnson", "alice@example.com");
            System.out.println("========================================\n");

            // 3. 사용자 삭제
            userService.deleteUser(2L);
            System.out.println("========================================\n");

            System.out.println("\n");
            System.out.println("=".repeat(60));
            System.out.println("장점: 비즈니스 로직만 3줄, 공통 관심사는 Aspect로 분리!");
            System.out.println("성능측정, 로깅, 예외처리, 보안 체크 모두 자동 적용!");
            System.out.println("=".repeat(60));
            System.out.println();
        };
    }
}
