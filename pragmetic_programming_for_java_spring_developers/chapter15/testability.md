# 15. 테스트 가능성
## 테스트가 왜 중요하냐
- 회귀 버그 방지
- 코드 품질 향상
  - 조건 : 테스트를 개발 전에 미리 작성하거나 개발을 하면서 함께 작성한다.
- 좋은 설계를 얻기 위한 수단으로 활용할 수 있음

## Testability (테스트 가능성)
- 높다 -> 테스트하기 쉽다. -> 대체로 좋은 설계일 확률이 높음
- 낮다 -> 테스트하기 어렵다. -> 대체로 좋지 않은 설계일 확률이 높음

# 15.1 테스트를 어렵게 만드는 요소
어떤 요소들이 테스트를 어렵게 만드는가?

## 15.1.1 숨겨진 입력
- 메서드를 실행하는데 필요하지만 외부에서는 이를 알 수 없는 감춰진 입력

### 숨겨진 입력 예제 코드 : 주사위 게임
- play 메서드 내부에서 주사위 객체를 생성하고 있음
- 승패 기준이 고정되어 있음
```java
public class DiceGameV1 {
    public void play() {
        SixFaceDice sixFaceDice = new SixFaceDice();
        int rollResult = sixFaceDice.roll();
        System.out.println("주사위 숫자는 " + rollResult + "입니다.");
        if (rollResult > 3) {
            System.out.println("당신이 이겼습니다.");
        } else {
            System.out.println("당신이 졌습니다.");
        }
    }
}
```

- 이게 왜 안 좋냐?
  - 숨겨진 입력이 들어가면 코드가 어떻게 동작할지 예상할 수 없음 == 코드 사용자가 코드를 제어할 수 없음
- 해결
  - 의존성 주입 : 숨겨진 입력을 제거하고 외부에서 주입할 수 있도록 변경
  - 의존성 역전 : 인터페이스로 추상화하여 의존 객체를 바꿀 수 있도록 변경

### 숨겨진 입력을 개선한 주사위 게임 
```java
public class DiceGameV2 {
  public void play(Dice dice, int threshold) {
    int rollResult = dice.roll();
    System.out.println("주사위 숫자는 " + rollResult + "입니다.");
    if (rollResult > threshold) {
      System.out.println("당신이 이겼습니다.");
    } else {
      System.out.println("당신이 졌습니다.");
    }
  }
}
```
```java
public class LoseDiceMock implements Dice {
    @Override
    public int roll() {
        return 1;
    }
}
```
- 어떤 코드가 더 나은 방식인지 고민? -> 그러면 테스트하기 쉬운 쪽으로 선택

## 15.1.2 숨겨진 출력
- 메서드 호출 결과가 반환값이 아닌 경우를 가리킴
- 반환값 외에 존재하는 모든 부수적인 출력을 숨겨진 출력이라고 함
- 완전히 없애기 어려움
  - 어딘가에서는 반드시 System.out으로 출력하는 코드가 있어야하기 때문
- 사이드 이펙트를 발생 시킴

### 숨겨진 출력 주사위 게임 코드
- 주사위 숫자와 승패 결과를 System.out.println을 통해 출력하고 있음
```java
public class DiceGameV2 {
  public void play(Dice dice, int threshold) {
    int rollResult = dice.roll();
    System.out.println("주사위 숫자는 " + rollResult + "입니다.");
    if (rollResult > threshold) {
      System.out.println("당신이 이겼습니다.");
    } else {
      System.out.println("당신이 졌습니다.");
    }
  }
}
```

- 해결
  - 실행 결과를 String으로 반환
    - 어떤 의도로 String으로 반환하는지 파악하기가 힘듦
    - 반환값을 위한 DTO를 만들어서 어떤 의도인지 명시적으로 표현
    - 이벤트 클래스 만들고 메서드의 반환값으로 이벤트 반환

### 실행 결과를 모아서 return 하는 주사위 게임 코드
```java
public class DiceGameV3 {
  public List<String> play(Dice dice, int threshold) {
    ArrayList<String> results = new ArrayList<>();
    int rollResult = dice.roll();
    results.add("주사위 숫자는 " + rollResult + "입니다.");
    if (rollResult > threshold) {
      results.add("당신이 이겼습니다.");
    } else {
      results.add("당신이 졌습니다.");
    }
    return results;
  }
}
```

### 반환값을 위한 DTO로 결과를 반환하는 주사위 게임 코드
```java
public class DiceGameV4 {
    public GameResult play(Dice dice, int threshold) {
        ArrayList<String> results = new ArrayList<>();
        int rollResult = dice.roll();
        if (rollResult > threshold) {
            return new GameResult(String.valueOf(rollResult), "당신이 이겼습니다.");
        } else {
            return new GameResult(String.valueOf(rollResult), "당신이 졌습니다.");
        }
    }
}
``` 

### 반환값을 위한 이벤트 클래스를 만들고 이벤트 리스트로 결과를 반환하는 주사위 게임 코드
```java
public class DiceGameV5 {
    public List<Event> play(Dice dice, int threshold) {
        ArrayList<Event> events = new ArrayList<>();
        int rollResult = dice.roll();
        events.add(new Event("주사위 결과", String.valueOf(rollResult)));
        if (rollResult > threshold) {
            events.add(new Event("게임 결과", "당신이 이겼습니다."));
        } else {
            events.add(new Event("게임 결과", "당신이 졌습니다."));
        }
        return events;
    }
}
```

### 함수형 프로그래밍
- 사이드 이펙트를 최소한으로 설계한 함수 

## 핵심
- 입출력을 강박적으로 제거할 필요는 없음
- 소프트웨어를 예측 가능하도록 만드는 것이 핵심

## 15.2 테스트가 보내는 신호
1. 테스트의 입출력 확인 불가
  - 테스트의 입출력을 확인할 수 있는 구조로 코드를 변경해야함
  - 숨겨진 입력을 외부로 드러내자
  - 숨겨진 출력은 반환값을 통해 출력되도록 변경하자
2. private 메서드 테스트
  - 테스트할 필요가 없음
  - 이걸 테스트하겠다는 생각이 든거면 책임 할당 잘못한거임
  - 해당 메서드의 코드를 다른 객체에 할당하고 그 메서드를 public으로 선언
3. 너무 많은 객체 주입
  - 서비스 컴포넌트 단위를 더 작게 나눠야할 시점임
3. 테스트 코드 커버리지 100%를 위한 많은 테스트 코드 필요
  - 긴 코드로 인해 테스트 케이스가 너무 많아짐
    - 해당 메서드에 책임이 너무 많은 것임
  - 테스트 코드 커버리지 100%가 중요한게 아님
  - 핵심은 시스템의 품질을 보장하고 개선하기 위함임
    - 테스트가 책임을 제대로 수행하고 검증하고 있는지 확인해라

### 이런 신호가 감지된다면?
- 설계가 잘못됐을 확률이 높다.
- 좋은 설계로 변경해야한다.

### 이런 신호를 감지하면 어떤게 좋은가?
- 코드 사용자의 입장에서 바라볼 수 있음
- 요구사항 위주로 바라볼 수 있음

### 테스트 프레임워크의 오해
- 테스트 프레임워크가 있어야지만 테스트할 수 있는 것이 아니다.
- 테스트 공부는 프레임워크 공부가 아니다.

### 테스트의 본질
- 어떤 것을 테스트해야 할지
- 어떻게 테스트해야 할지
- 어떻게 코드를 작성해야 테스트가 쉬워질지