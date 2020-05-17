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



```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ArticleControllerTests {
    ...
}
```



#### webEnvironment

`@SpringBootTest`는 기본적으로 웹서버를 실행시키지 않는다. `webEnvironment`설정을 통해 애플리케이션이 실행될 때 웹 환경을 설정할 수 있다. 

* MOCK : 웹 ApplicationContext를 로드하고 모의 웹 환경을 제공. 내장 서버가 시작되지 않는다. 클래스 경로에서 웹 환경을 사용할 수없는 경우이 모드는 웹 이외의 일반 ApplicationContext를 작성하는 것으로 대체된다. 웹 응용 프로그램의 모의 기반 테스트를 위해 `@AutoConfigureMockMvc` 또는 `@AutoConfigureWebTestClient`와 함께 사용할 수 있다.
* RANDOM_PORT : WebServerApplicationContext를 로드하고 실제 웹 환경을 제공. 내장 서버가 시작되고 임의 포트가 실행.
* DEFINED_PORT : WebServerApplicationContext를 로드하고 실제 웹 환경을 제공. 임베디드 서버가 시작되어 정의 된 포트 (application.properties에서) 또는 기본 포트 8080로 실행.
* NONE : SpringApplication을 사용하여 ApplicationContext를로드하지만 웹 환경 (모의 또는 기타)을 제공하지 않음.

> By default, `@SpringBootTest` will not start a server. You can use the `webEnvironment` attribute of `@SpringBootTest` to further refine how your tests run:
>
> - `MOCK`(Default) : Loads a web `ApplicationContext` and provides a mock web environment. Embedded servers are not started when using this annotation. If a web environment is not available on your classpath, this mode transparently falls back to creating a regular non-web `ApplicationContext`. It can be used in conjunction with [`@AutoConfigureMockMvc` or `@AutoConfigureWebTestClient`](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-with-mock-environment) for mock-based testing of your web application.
> - `RANDOM_PORT`: Loads a `WebServerApplicationContext` and provides a real web environment. Embedded servers are started and listen on a random port.
> - `DEFINED_PORT`: Loads a `WebServerApplicationContext` and provides a real web environment. Embedded servers are started and listen on a defined port (from your `application.properties`) or on the default port of `8080`.
> - `NONE`: Loads an `ApplicationContext` by using `SpringApplication` but does not provide *any* web environment (mock or otherwise).
>
> [spring boot docs](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications)



### @AutoConfigureWebTestClient

테스트하려는 웹 엔드포인트가 있는 경우, `webTestClent`를 사용하고 싶으면 `@AutoConfigureWebTestClient` 어노테이션을 사용하면 된다.

```java
@SpringBootTest
@AutoConfigureWebTestClient
class MockWebTestClientExampleTests {

    @Test
    void exampleTest(@Autowired WebTestClient webClient) {
        webClient.get().uri("/").exchange().expectStatus().isOk().expectBody(String.class).isEqualTo("Hello World");
    }

}
```

> [spring boot docs](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-with-mock-environment)

비동기라는 점을 인지하고 테스트를 작성해야한다.