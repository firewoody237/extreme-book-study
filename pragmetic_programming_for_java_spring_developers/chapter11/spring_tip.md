## 11-1. 타입 기반 주입
- `@Autowired`는 타입에 기반하여 Bean을 찾고, 찾지 못하면 `NoSuchBeanDefinitionException`을 던짐
  - 중복으로 찾게 되면, `NoUniqueBeanDefinitionException`을 던짐
- `List`형식으로 특정타입의 여러 Bean을 주입받을 수 있음
  - 이 형식으로 받음으로써 OCP를 지킬 수 있음
```java
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final List<NotificationChannel> notificationChannels;

    public void notify(Account account, String message) {
        for (NotificationChannel notificationChannel : notificationChannels) {
            if (notificationChannel.supports(account)) {
                notificationChannel.notify(account, message);
            }
        }
    }
}
```

## 11-2. 자가 호출
- Spring은 `CGLIB`을 통해 프록시 기술을 사용
  - AOP등의 사용에 활용
> 프록시가 여러개면, 내부적으로 Advice의 체이닝을 수행하는 식으로 동작
```java
public void sample() {
    List<MethodInterceptor> chain = ...;
    MethodInvocation invocation = ...;
    
    try {
        invocation.proceed();
    } catch (...) {
        ...
    }
}
```
