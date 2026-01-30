package com.example.without;

import com.example.without.model.User;
import com.example.without.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService) {
        return args -> {
            System.out.println("\n");
            System.out.println("=".repeat(60));
            System.out.println("WITHOUT AOP - 중복 코드가 가득한 버전");
            System.out.println("=".repeat(60));
            System.out.println();

            // 1. 사용자 조회
            User user = userService.getUser(1L);

            // 2. 사용자 생성
            User newUser = userService.createUser("Alice Johnson", "alice@example.com");

            // 3. 사용자 삭제
            userService.deleteUser(2L);

            System.out.println("\n");
            System.out.println("=".repeat(60));
            System.out.println("문제점: 각 메서드마다 25줄 이상의 중복 코드!");
            System.out.println("비즈니스 로직은 단 2-3줄인데 공통 관심사가 25줄...");
            System.out.println("=".repeat(60));
            System.out.println();
        };
    }
}
