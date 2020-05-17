# jwp-blog
나만의 블로그를 만들어봅시다.

heebong - Step1. 게시글 생성 조회

[Step1 피드백](https://github.com/woowacourse/jwp-blog/pull/6)

* springboot 사용
* get/post 구현
* webTestClient로 테스트 구현



## 테스트

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MyblogApplicationTests {

    @Test
    void contextLoads() {
    }

}
```

### @ExtendWith(SpringExtension.class) - SpringBootTest와 JUnit의 연결 고리

> `@ExtendWith(SpringExtension.class)`은 Junit5의 어노테이션으로, JUnit4의 `@RunWith(SpringRunner.class)` 어노테이션과 같다.

> If you are using JUnit 4, don’t forget to also add `@RunWith(SpringRunner.class)` to your test, otherwise the annotations will be ignored. If you are using JUnit 5, there’s no need to add the equivalent `@ExtendWith(SpringExtension.class)` as `@SpringBootTest` and the other `@…Test` annotations are already annotated with it.
>
> [spring boot docs](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing)

SpringBootTest 기능과 JUnit 기능을 연결하는 다리 역할을 하는 어노테이션.

`@ExtendWith` 어노테이션을 사용하면 JUnit에 내장된 러너를 사용하는 대신 어노테이션에 정의된 러너 클래스를 사용한다. `@SpringBootTest` 어노테이션을 사용하려면 JUnit 실행에 필요한 `@ExtendWith(SpringExtenstion.class)`를 꼭 붙여야 한다.

즉, SpringBootTest에서 JUnit 기능을 사용하고 싶다면 꼭 붙여야 한다.

> *@RunWith(SpringRunner.class)* is used to provide a bridge between Spring Boot test features and JUnit. Whenever we are using any Spring Boot testing features in our JUnit tests, this annotation will be required.
>
> [baeldung](https://www.baeldung.com/spring-boot-testing)



> 스프링과 관련된 경우라면 러너로 `SpringExtenstion.class`를 사용해야하고 다른 경우, 예를 들어 Mock 관련 어노테이션만 사용하고 싶은 경우 `MockitoExtenstion.class`를 사용하면 된다.





### @SpringBootTest - 똑같이 애플리케이션 컨텍스트 로드

`@SpringBootTest` 는 통합 테스트를 제공하는 기본적인 스프링부트 어노테이션.

여러 단위의 테스트를 하나의 통합된 테스트로 수행할 때 적합.

실제 구동되는 애플리케이션과 똑같이 애플리케이션 컨텍스트를 로드해 테스트를 한다.

> The *@SpringBootTest* annotation can be used when we need to bootstrap the entire container. The annotation works by creating the *ApplicationContext* that will be utilized in our tests.
>
> [baeldung](https://www.baeldung.com/spring-boot-testing)