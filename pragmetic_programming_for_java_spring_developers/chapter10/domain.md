## 10-1. 소프트웨어 개발의 시작
- 비즈니스? : 사용자가 겪는 문제를 해결해주는 것
- 린 방식 업무 스타일? : 사용자가 겪는 문제와 해결책을 강조
- 도메인? : 사용자가 겪는 문제 영역
- 프로덕트의 품질을 올리려면, 도메인에 대한 깊은 이해를 해야 함

## 10-2. 애플리케이션의 본질
- 어떠한 개발을 진행할 때, 도메인을 먼저 생각해야 함
- 우선 도메인을 분석하고, 도메인의 요구사항을 정리하는 것이 먼저여야 함
- 프로젝트의 구현이나 도구는 나중에 고민해도 괜찮은 선택사항
  - 결국에 구현은 도메인의 유형에따라 바뀔 수 있는 요소
  - 순수하게 도메인에 집중해야, 결과물이 프레임워크나 도구에 의존적이지 않게 됨
- 자신이 어떤 도메인을 다루는지 알 수 있는 '소리치는 아키텍처'를 만들어야 함
- 도메인을 제외한 다른 부분은 도메인을 해결하기 위한 수단일 뿐

## 10-3. 도메인 모델과 영속성 객체
### 통합하기 전략
- 도메인 모델과 영속성 객체를 하나의 클래스로 관리
```java
@Data
@Entity(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column
    private String nickname;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
```
- 장점
  - 빠른 개발속도
  - 자연스럽게 ORM의 특성을 사용
- 단점
  - 클래스의 책임이 제대로 눈에 들어오지 않음
  - 데이터베이스 위주의 사고로 빠지기 쉬움

### 구분하기 전략
- 도메인 모델과 영속성 객체를 나눔
```java
@Builder
public class Account {
    private Long id;
    private String email;
    private String nickname;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
```

```java
@Data
@Entity(name = "account")
public class AccountJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column
    private String nickname;

    public static AccountJpaEntity from(Account account) {
        AccountJpaEntity result = new AccountJpaEntity();
        result.id = account.getId();
        result.email = account.getEmail();
        result.nickname = account.getNickname();
        return result;
    }

    public Account toModel() {
        return Account.builder()
            .id(this.id)
            .email(this.email)
            .nickname(this.nickname)
            .build();
    }
}
```
- 장점
  - ORM에 종속적이지 않음
- 단점
  - 개발 비용 증가
    - 유사한 모델을 두 번 만들어야 함
    - 영속성 <-> 도메인 모델간 매핑 메서드 추가 필요
  - ORM의 혜택을 누리기 어려움
> 영속성 컨텍스트의 더티체킹을 쓰려면 코드가 더 늘어나고, 놓치는 부분이 생길수도 있을 것 같음
> 위 코드 같은 경우는 더티체킹을 쓰려면 Setter들이 외부에서 호출되어야 하므로 누락이 많이 발생할 것 같음
> 예를 들면, JPA에서 읽어오고, Domain으로 바꾼 후 이런저런 작업을 한후 적용할 때
>   객체를 계속 가지고 있다가 변경분만 Setter를 쓰거나 --> 누락 발생
>   `from`을 호출해야 하는데 --> 더티체킹 활용 X

> 그럼 ORM의 장점을 버린채 개발해야 하는걸까? 어떤 때 온전히 JPA를 사용해도 좋을걸까?
