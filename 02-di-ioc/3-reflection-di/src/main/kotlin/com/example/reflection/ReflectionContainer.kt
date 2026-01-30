package com.example.reflection

import com.example.reflection.annotation.Component
import com.example.reflection.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

/**
 * Level 3: Reflection 기반 DI 컨테이너
 *
 * 기능:
 * 1. 패키지 스캔으로 @Component 자동 발견
 * 2. Reflection으로 객체 자동 생성
 * 3. 생성자의 @Inject 파라미터 자동 주입
 * 4. 의존성 순서 자동 해결
 *
 * Spring의 ApplicationContext와 유사한 역할
 */
class ReflectionContainer(basePackage: String) {
    private val beans = mutableMapOf<KClass<*>, Any>()
    private val interfaceToImpl = mutableMapOf<KClass<*>, KClass<*>>()
    private val componentClasses = mutableListOf<KClass<*>>()

    init {
        println("=== 컴포넌트 스캔 시작: $basePackage ===")
        scanComponents(basePackage)
        println()
        println("=== Bean 생성 시작 ===")
        createBeans()
        println()
    }

    /**
     * 1단계: 패키지 스캔
     * @Component가 붙은 모든 클래스를 찾음
     */
    private fun scanComponents(basePackage: String) {
        // 실제 Spring은 ClassPathScanningCandidateComponentProvider를 사용
        // 여기서는 간단히 구현
        val packagePath = basePackage.replace('.', '/')
        val classLoader = Thread.currentThread().contextClassLoader
        val resources = classLoader.getResources(packagePath)

        val classes = mutableListOf<KClass<*>>()

        resources.asSequence().forEach { url ->
            val path = url.path
            val dir = java.io.File(path)
            if (dir.exists() && dir.isDirectory) {
                findClassesInDirectory(dir, basePackage, classes)
            }
        }

        classes.forEach { clazz ->
            if (clazz.findAnnotation<Component>() != null) {
                println("[Scan] ${clazz.simpleName} 발견")

                // 모든 컴포넌트 클래스 저장
                componentClasses.add(clazz)

                // 인터페이스 매핑 저장
                clazz.java.interfaces.forEach { intf ->
                    interfaceToImpl[intf.kotlin] = clazz
                }
            }
        }
    }

    /**
     * 디렉토리에서 클래스 파일 찾기
     */
    private fun findClassesInDirectory(
        directory: java.io.File,
        packageName: String,
        classes: MutableList<KClass<*>>
    ) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                findClassesInDirectory(file, "$packageName.${file.name}", classes)
            } else if (file.name.endsWith(".class")) {
                val className = "$packageName.${file.name.substring(0, file.name.length - 6)}"
                try {
                    val clazz = Class.forName(className).kotlin
                    classes.add(clazz)
                } catch (e: Exception) {
                    // 내부 클래스나 로드 불가능한 클래스는 무시
                }
            }
        }
    }

    /**
     * 2단계: Bean 생성
     * 의존성 순서를 고려하여 객체 생성
     */
    private fun createBeans() {
        componentClasses.forEach { clazz ->
            if (!beans.containsKey(clazz)) {
                createBean(clazz)
            }
        }
    }

    /**
     * Bean 생성 (재귀적으로 의존성 해결)
     */
    private fun createBean(clazz: KClass<*>): Any {
        // 이미 생성된 Bean은 재사용 (싱글톤)
        beans[clazz]?.let { return it }

        println("[Create] ${clazz.simpleName} 생성 중...")

        val constructor = clazz.primaryConstructor
            ?: throw IllegalStateException("${clazz.simpleName}에 Primary Constructor가 없습니다")

        // 생성자 파라미터 분석
        val parameters = constructor.parameters
        val args = mutableListOf<Any?>()

        parameters.forEach { param ->
            val hasInject = param.findAnnotation<Inject>() != null
            if (hasInject) {
                // @Inject가 있으면 자동 주입
                val dependency = resolveDependency(param)
                args.add(dependency)
                println("  [Inject] ${param.name}: ${dependency::class.simpleName}")
            } else {
                // @Inject가 없으면 null (선택적 의존성)
                args.add(null)
            }
        }

        // 객체 생성
        val instance = constructor.call(*args.toTypedArray())
        beans[clazz] = instance

        // 인터페이스 타입으로도 등록
        clazz.java.interfaces.forEach { intf ->
            beans[intf.kotlin] = instance
        }

        println("  [Done] ${clazz.simpleName} 생성 완료")
        return instance
    }

    /**
     * 의존성 해결
     * 파라미터 타입에 맞는 Bean 찾기
     */
    private fun resolveDependency(param: KParameter): Any {
        val type = (param.type.javaType as Class<*>).kotlin

        // 이미 생성된 Bean이 있으면 반환
        beans[type]?.let { return it }

        // 인터페이스인 경우 구현체 찾기
        val implClass = interfaceToImpl[type]
            ?: throw IllegalStateException("${type.simpleName}에 대한 구현체를 찾을 수 없습니다")

        // 재귀적으로 의존성 생성
        return createBean(implClass)
    }

    /**
     * Bean 조회
     */
    fun <T : Any> getBean(type: KClass<T>): T {
        @Suppress("UNCHECKED_CAST")
        return (beans[type] ?: beans[interfaceToImpl[type]]) as? T
            ?: throw IllegalStateException("${type.simpleName}을 찾을 수 없습니다")
    }

    /**
     * 등록된 Bean 목록 출력
     */
    fun printBeans() {
        println("=== 등록된 Bean 목록 ===")
        beans.forEach { (clazz, instance) ->
            println("  ${clazz.simpleName} -> ${instance::class.simpleName}")
        }
        println()
    }
}

/**
 * Kotlin DSL을 위한 확장 함수
 */
inline fun <reified T : Any> ReflectionContainer.getBean(): T {
    return getBean(T::class)
}
