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

```java
@DisplayName("RangeChecker Right-BICEP 테스트")
class RangeChecker_RightBICEP_Test {

    private final RangeChecker checker = new RangeChecker();
    private final int MIN_VAL = 10;
    private final int MAX_VAL = 100;

    @Nested
    @DisplayName("[R] Right - 결과가 올바른가?")
    class RightTests {
        @Test
        @DisplayName("범위 안의 대표 값은 true를 반환한다")
        void typicalValueInsideRange_ShouldReturnTrue() {
            assertThat(checker.isInRange(50, MIN_VAL, MAX_VAL)).isTrue();
        }

        @Test
        @DisplayName("범위 밖 (아래)의 대표 값은 false를 반환한다")
        void typicalValueBelowRange_ShouldReturnFalse() {
            assertThat(checker.isInRange(MIN_VAL - 10, MIN_VAL, MAX_VAL)).isFalse();
        }

        @Test
        @DisplayName("범위 밖 (위)의 대표 값은 false를 반환한다")
        void typicalValueAboveRange_ShouldReturnFalse() {
            assertThat(checker.isInRange(MAX_VAL + 10, MIN_VAL, MAX_VAL)).isFalse();
        }
    }

    @Nested
    @DisplayName("[B] Boundary - 경계 조건은 어떤가?")
    class BoundaryTests {
        @Test
        @DisplayName("최소 경계값은 true를 반환한다")
        void minValue_ShouldReturnTrue() {
            assertThat(checker.isInRange(MIN_VAL, MIN_VAL, MAX_VAL)).isTrue();
        }

        @Test
        @DisplayName("최대 경계값은 true를 반환한다")
        void maxValue_ShouldReturnTrue() {
            assertThat(checker.isInRange(MAX_VAL, MIN_VAL, MAX_VAL)).isTrue();
        }

        @Test
        @DisplayName("최소 경계 바로 아래 값은 false를 반환한다")
        void valueJustBelowMin_ShouldReturnFalse() {
            assertThat(checker.isInRange(MIN_VAL - 1, MIN_VAL, MAX_VAL)).isFalse();
        }

        @Test
        @DisplayName("최대 경계 바로 위 값은 false를 반환한다")
        void valueJustAboveMax_ShouldReturnFalse() {
            assertThat(checker.isInRange(MAX_VAL + 1, MIN_VAL, MAX_VAL)).isFalse();
        }

        @Test
        @DisplayName("최소값과 최대값이 같고 그 값일 때 true를 반환한다")
        void minEqualsMaxAndValueIsAtBoundary_ShouldReturnTrue() {
            assertThat(checker.isInRange(50, 50, 50)).isTrue();
        }
    }

    @Nested
    @DisplayName("[I] Inverse - 역 관계는 성립하는가?")
    class InverseTests {
        // 이 케이스에는 "역"관련이 없어서 isFalse()로 뒤집었다고 함
        @Test
        @DisplayName("범위 밖의 값이 false인지 확인 (isFalse 검증)")
        void outOfRangeValue_CheckUsingIsFalse() {
            assertThat(checker.isInRange(MAX_VAL + 1, MIN_VAL, MAX_VAL)).isFalse();
        }

        @Test
        @DisplayName("범위 안의 값이 true인지 확인 (isTrue 검증)")
        void inRangeValue_CheckUsingIsTrue() {
            assertThat(checker.isInRange(MIN_VAL + 1, MIN_VAL, MAX_VAL)).isTrue();
        }
        // 참고: 더 복잡한 로직의 경우, 특정 연산 후 역연산을 수행하여 원래 상태로 돌아오는지 등을 검증할 수 있습니다.
    }

    @Nested
    @DisplayName("[C] Cross-checking - 다른 결과와 교차 확인하는가?")
    class CrossCheckingTests {
        @Test
        @DisplayName("간단한 수동 로직과 결과 비교")
        void crossCheckWithManualLogic() {
            int testNum = 77;
            // 동일 로직을 테스트 코드 내에서 다르게 구현하여 비교
            boolean expectedResult = (testNum >= MIN_VAL) && (testNum <= MAX_VAL);
            System.out.println("[C] Cross-check for " + testNum + ": Manual logic expects " + expectedResult);

            assertThat(checker.isInRange(testNum, MIN_VAL, MAX_VAL))
                    .isEqualTo(expectedResult);
        }

        @Test
        @DisplayName("다른 데이터 소스나 레거시 함수와 비교 (개념 예시)")
        void conceptualCrossCheckWithLegacy() {
            // 실제 레거시 함수나 DB 값이 있다면 여기서 비교
            // Assume legacyCheck(num, min, max) exists
            int testNum = 55;
            // boolean legacyResult = legacyCheck(testNum, MIN_VAL, MAX_VAL);
            // assertThat(checker.isInRange(testNum, MIN_VAL, MAX_VAL)).isEqualTo(legacyResult);
            System.out.println("[C] Conceptual: Cross-check with legacy function would happen here.");
            assertThat(true).isTrue(); // 실제 비교 로직 없으므로 통과 처리
        }
    }

    @Nested
    @DisplayName("[E] Error - 오류 조건을 강제할 수 있는가?")
    class ErrorConditionTests {
        @Test
        @DisplayName("min > max 조건에서 IllegalArgumentException 발생")
        void minGreaterThanMax_ShouldThrowIllegalArgumentException() {
            int invalidMin = 101;
            int invalidMax = 100;

            // AssertJ 사용
            assertThatThrownBy(() -> checker.isInRange(50, invalidMin, invalidMax))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("min (101) cannot be greater than max (100)");

            // JUnit 5 기본 Assertions 사용 (대안)
            // assertThrows(IllegalArgumentException.class,
            //              () -> checker.isInRange(50, invalidMin, invalidMax));
        }

        // 참고: 입력 파라미터 타입이 int이므로 null 이나 문자열 입력 테스트는 컴파일 단계에서 막힙니다.
        // 만약 외부 입력(String 등)을 받아 int로 변환하는 로직이 있다면, 그 변환 과정에서의 오류 테스트가 필요합니다.
    }

    @Nested
    @DisplayName("[P] Performance - 성능 특성은 기준을 만족하는가?")
    class PerformanceTests {

        @Test
        @Timeout(value = 50, unit = TimeUnit.MILLISECONDS) // 50ms 안에 끝나야 함 (매우 관대함)
        @DisplayName("단일 호출이 매우 빠르게 완료되어야 한다 (@Timeout)")
        void singleCall_ShouldCompleteQuickly() {
            // @Timeout 은 지정된 시간 내에 테스트 메소드 실행이 완료되는지 확인
            checker.isInRange(50, 1, Integer.MAX_VALUE -1); // 큰 범위로 테스트
        }

        @Test
        @DisplayName("많은 반복 호출 시 지정된 시간 내 완료 (명시적 시간 측정)")
        void multipleCalls_ShouldCompleteWithinTimeLimit() {
            int iterations = 1_000_000;
            long maxDurationMillis = 100; // 100ms 제한

            // JUnit 5의 assertTimeoutPreemptively 사용
            assertTimeoutPreemptively(Duration.ofMillis(maxDurationMillis), () -> {
                for (int i = 0; i < iterations; i++) {
                    // 루프 내에서 다양한 값으로 테스트
                    checker.isInRange( (MIN_VAL + i) % (MAX_VAL + 1), MIN_VAL, MAX_VAL);
                }
            }, "실행 시간이 " + maxDurationMillis + "ms를 초과했습니다.");

             System.out.println("[P] Performance: " + iterations + " iterations completed within " + maxDurationMillis + " ms.");
            // 참고: 정확한 벤치마킹에는 JMH(Java Microbenchmark Harness) 사용이 권장됩니다.
        }
    }
}
```

### CORRECT
1. Conformance(적합성) : 데이터 포맷이 제대로 처리되는지 확인
   1. ex. email, 전화번호 등
2. Ordering(정렬) : 출력에 순서가 보장돼야 한다면 확인
3. Range(범위) : 입력에 양 끝점이 있다면 양 끝점이 들어갈 때 정상 동작하는지 확인
   1. ex. 나이가 음수인가?
4. Reference(참조) : 협력 객체의 상태에 따라 어떻게 동작하는지 확인
   1. ex. 사전/사후 조건 등
5. Existence(존재) : null, blank 같은 값이 입력될 때 어떻게 반응하는지 확인
6. Cradinality(원소 개수) : 입력의 개수가 0, 1, 2, ..., n일 때 어떻게 동작하는지 확인
7. Time(시간) : 병렬 처리를 한다면 순서가 보장되는지 확인
   1. 상대적 시간 : 호출 순서 등
   2. 절대적 시간 : 타임존, 써머타임 등
   3. 동시성 문제 : 병렬처리

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