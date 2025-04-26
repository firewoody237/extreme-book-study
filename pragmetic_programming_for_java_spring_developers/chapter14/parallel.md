# 테스트 병렬 수행
- `JUnit5`부터 도입

## Properties
- 활성화
```properties
# 병렬 실행 활성화
junit.jupiter.execution.parallel.enable = true
```

- 클래스 레벨 병렬 실행
```properties
# 병렬 실행 안함(기본값)
junit.jupiter.execution.parallel.mode.default = same_thread
# 병렬 실행 함
junit.jupiter.execution.parallel.mode.default = concurrent
```

- 메서드 레벨 병렬 실행
  - 메서드간 상태 공유가 없어야 안전
```properties
# 클래스 내 메서드는 순차 실행 (기본값)
junit.jupiter.execution.parallel.mode.classes.default = same_thread
# 클래스 내 메서드도 병렬 실행
junit.jupiter.execution.parallel.mode.classes.default = concurrent
```

- 병렬 실행 전략(스레드 풀 관리 방식)
```properties
# dynamic
# 사용 가능한 프로세서 코어 수에 기반하여 동적으로 스레드 수 결정
junit.jupiter.execution.parallel.config.strategy = dynamic
# 하나의 CPU에서 사용할 스레드 개수 설정
junit.jupiter.execution.parallel.config.dynamic.factor = 1

# fixed
# 고정된 수의 스레드 사용
junit.jupiter.execution.parallel.config.strategy = fixed
# 고정 스레드 수 지정
junit.jupiter.execution.parallel.config.fixed.parallelism = 4

# custom
# 사용자 정의 전략 사용
junit.jupiter.execution.parallel.config.strategy = custom
```

### custom일 때
- `ParallelExecutionConfigurationStrategy`를 구현하고 등록하여 사용한다.
### 구현 예시
```java
import org.junit.platform.engine.ConfigurationParameters; // ConfigurationParameters 임포트 추가
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

import java.util.Optional;

public class MyCustomParallelStrategy implements ParallelExecutionConfigurationStrategy {

    // 사용자 정의 시스템 프로퍼티 키
    private static final String CUSTOM_PARALLELISM_PROPERTY = "my.custom.parallelism";
    // 기본 유지 시간 (초 단위)
    private static final int DEFAULT_KEEP_ALIVE_SECONDS = 60;

    /**
     * 병렬 실행에 사용할 스레드 수를 결정합니다.
     * 시스템 프로퍼티 'my.custom.parallelism' 값을 우선 읽고, 없으면 CPU 코어 수를 반환합니다.
     * @param configurationParameters JUnit 플랫폼 설정 파라미터 (여기서는 직접 사용하지 않음)
     * @return 계산된 병렬 스레드 수
     */
    @Override
    public int getParallelism(ConfigurationParameters configurationParameters) {
        // 시스템 프로퍼티에서 값을 읽어옵니다.
        Optional<String> customParallelism = configurationParameters.get(CUSTOM_PARALLELISM_PROPERTY);

        if (customParallelism.isPresent()) {
            try {
                int parallelism = Integer.parseInt(customParallelism.get());
                if (parallelism > 0) {
                    System.out.println("[MyCustomParallelStrategy] Using custom parallelism from property '"
                            + CUSTOM_PARALLELISM_PROPERTY + "': " + parallelism);
                    return parallelism;
                } else {
                    System.err.println("[MyCustomParallelStrategy] Invalid value for property '"
                            + CUSTOM_PARALLELISM_PROPERTY + "': " + customParallelism.get() + ". Using default.");
                }
            } catch (NumberFormatException e) {
                System.err.println("[MyCustomParallelStrategy] Could not parse property '"
                        + CUSTOM_PARALLELISM_PROPERTY + "' value: " + customParallelism.get() + ". Using default.");
            }
        }

        // 프로퍼티가 없거나 유효하지 않으면 기본값 (CPU 코어 수) 사용
        int defaultParallelism = Runtime.getRuntime().availableProcessors();
        System.out.println("[MyCustomParallelStrategy] Using default parallelism (available processors): " + defaultParallelism);
        return defaultParallelism;
    }

    /**
     * 스레드 풀에서 유휴 스레드를 유지할 시간(초)을 반환합니다.
     * @param configurationParameters JUnit 플랫폼 설정 파라미터 (여기서는 직접 사용하지 않음)
     * @return 유지 시간(초)
     */
    @Override
    public int getKeepAliveSeconds(ConfigurationParameters configurationParameters) {
        // 여기서는 간단히 고정된 값을 반환합니다. 필요시 이 값도 설정에서 읽어올 수 있습니다.
        System.out.println("[MyCustomParallelStrategy] Using keep alive seconds: " + DEFAULT_KEEP_ALIVE_SECONDS);
        return DEFAULT_KEEP_ALIVE_SECONDS;
    }

    // --- ParallelExecutionConfiguration 인터페이스 구현 ---
    // ParallelExecutionConfigurationStrategy는 ParallelExecutionConfiguration도 구현해야 합니다.
    // 위에서 구현한 메서드들이 ParallelExecutionConfiguration의 요구사항도 만족시킵니다.
    // 따라서 별도로 추가 구현할 메서드는 없습니다.
}
```
### 서비스 등록
- `src/test/resources/META-INF/services` 디렉토리 생성
- 디렉토리 내부에 다음 파일 생성 `org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy`
- 파일 내부에 패키지 전체를 포함한 구현 클래스 이름 기재
  - ex. `com.yourcompany.testing.MyCustomParallelStrategy`

## Annotation
- `@Execution(ExecutionMode.CONCURRENT)`
  - 사용한 클래스나 메서드를 기본 설정과 관계없이 병렬로 실행 강제
- `@Execution(ExecutionMode.SAME_THREAD)`
  - 사용한 클래스나 메서드를 기본 설정과 관계없이 순차로 실행 강제
- `@ResourceLock`
  - 테스트 간 공유 자원에 대한 접근을 동기화해야 할때 사용
  - 특정 자원을 사용하는 테스트들이 동시에 실행되지 않도록 제어
