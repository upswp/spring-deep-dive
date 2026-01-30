package com.example.reflection.annotation

/**
 * 컴포넌트를 표시하는 어노테이션
 * 이 어노테이션이 붙은 클래스는 자동으로 컨테이너에 등록됨
 *
 * Spring의 @Component, @Service, @Repository와 유사
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Component

/**
 * 의존성 주입을 표시하는 어노테이션
 * 생성자 파라미터에 붙이면 자동으로 주입됨
 *
 * Spring의 @Autowired와 유사
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
