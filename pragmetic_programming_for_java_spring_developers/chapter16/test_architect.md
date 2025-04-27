# 테스트와 설계
- 테스트와 소프트웨어 설계는 상호보완적
- 테스트하기 쉬운 코드는 높은 확률로 좋은 설계

## 16.1 테스트와 SRP
- 테스트 코드를 만들다 보면, 불필요한 의존성을 넣어야 하는 경우가 발생
  - SRP 원칙을 통해 역할을 분리

## 16.2 테스트와 ISP
- `Dummy` 혹은 `Fake` 객체를 만들 때, 인터페이스의 사용하지 않는 다른 메서드들을 구현해야 하는 경우가 발생
  - 인터페이스가 통합되어있는 문제
  - 인터페이스를 분리
  - 분리된 인터페이스를 하나의 구현체에 구현함으로써 컴포넌트의 파편화 방지
> 이런 식이면 모든 인터페이스들이 `Functional`하게 되어버리지 않을까? 과도한 설계 아닐까?

## 16.3 테스트와 OCP, DIP
- 테스트를 이용하면 코드의 유연성과 확장성이 어떤지 판단할 수 있음
> 테스트는 "사용하는 코드"이기 때문에, 다른 사용하는 코드들의 입장에도 마찬가지로 유연성과 확장성이 오름

## 16.4 테스트와 LSP
- 어떤 것을 테스트할것인가?
### Right-BICEP
1. Right : 결과가 올바른지 확인
2. Boundary : 경계 조건에서 코드가 정상적으로 동작하는지 확인
3. Inverse : 역함수가 있다면 이를 실행해 입력과 일치하는지 확인
4. Cross-Check : 검증에 사용할 다른 수단이 있다면 이를 비교
5. Error Conditions : 오류 상황에서도 프로그램이 의도한 동작을 하는지 확인
6. Performance : 프로그램이 예상한 성능 수준을 유지하는지 확인

### CORRECT
1. Conformance(적합성) : 데이터 포맷이 제대로 처리되는지 확인
2. Ordering(정렬) : 출력에 순서가 보장돼야 한다면 확인
3. Range(범위) : 입력에 양 끝점이 있다면 양 끝점이 들어갈 때 정상 동작하는지 확인
4. Reference(참조) : 협력 객체의 상태에 따라 어떻게 동작하는지 확인
5. Existence(존재) : null, blank 같은 값이 입력될 때 어떻게 반응하는지 확인
6. Cradinality(원소 개수) : 입력의 개수가 0, 1, 2, ..., n일 때 어떻게 동작하는지 확인
7. Time(시간) : 병렬 처리를 한다면 순서가 보장되는지 확인

### LSP
- 파생 클래스의 테스트가 추상 클래스를 상속하게 하면 동시에 상위에 선언된 테스트코드도 실행할 수 있음
  - 아래 코드는 `RectangleTest`, `SquareTest` 모두 실행 됨

```java
public abstract class RectangleLiskovTest {
    abstract Rectangle createSystemUnderTest();
    abstract long getSystemUnderTestArea();

    @Test
    public void 넓이를_계산할_수_있다() {
        Rectangle rectangle = createSystemUnderTest();

        long result = rectangle.calculateArea();

        assertThat(result).isEqualTo(getSystemUnderTestArea());
    }
}
```

```java
public class RectangleTest extends RectangleLiskovTest {
    @Override
    public Rectangle createSystemUnderTest() {
        return new Rectangle(10, 5);
    }

    @Override
    public long getSystemUnderTestAream() {
        return 50;
    }
}

public class SquareTest extends RectangleLiskovTest {
    @Override
    public Rectangle createSystemUnderTest() {
        return new Square(10);
    }

    @Override
    public long getSystemUnderTestAream() {
        return 100;
    }
}
```