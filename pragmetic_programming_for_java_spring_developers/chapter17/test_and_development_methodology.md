# 17. 테스트와 개발 방법론

## TDD (test-driven development : 테스트 주도 개발)  
- 코드를 작성하기 전에 해당 코드의 테스트 케이스를 먼저 작성 한 후 해당 테스트를 통과할 수 있도록 코드를 작성하는 방식

## BDD (behavior-driven development : 행동 주도 개발)
- 소프트웨어 개발 과정에서 비즈니스 요구사항과 소프트웨어 행동을 강조하는 개발 방법론

# 17.1 TDD
## 3단계
테스트를 먼저 작성한다(Red) -> 기능을 구현한다(Green) -> 리펙터링 한다(Blue)
- Red 단계
  - 아직 구현되지 않은 기능을 테스트하는 케이스를 작성함
- Green 단계
  - 테스트를 통과시키기 위한 최소한의 코드를 작성
- Blue 단계 (=Refactor 단계)
  - Green 단계에서 작성한 코드를 리팩터링
  - 코드의 가독성과 유지보수, 성능을 높이는데 집중
  - 리팩터링으로 인해 기능의 동작이 변경돼서는 안됨

## 장점
테스트 코드를 작성하면 얻는 장점을 그대로 가짐
- 소프트웨어 기능을 견고하게 유지
- 오류가 생겼을 때 빠르게 감지해 이를 수정

## 단점
- TDD를 적용하는 것 자체가 어려움 (모든 팀원이 테스트 코드 작성에 숙련된 상태여야함)
- 초기 개발 속도가 느려짐
  - 반박
    - 짧은 피드백 루프를 통해 조기 문제 발견 -> 디버깅 시간을 줄일 수 있음
    - 테스트 코드를 통해 코드 변경에 대한 부담감을 덜 수 있음
    - 테스트 코드가 문서 역할을 함 -> 시스템 확장이 병렬로 이뤄질 수 있음

## TDD가 만능이고 모든 프로젝트에 도입해야하나?
**TDD는 만능이 아니다. 상황에 따라 도입해야한다.**
- 요구사항이 명확하지 않거나 자주 바뀔 때 -> 구현 전 작성해 놓은 테스트 코드가 무용지물이됨
- 시장 반응을 살피기 위해 빠른 개발과 배포가 필요할 때 -> TDD로 인해 초기 개발 속도가 느려질 수 있음

## 그럼 어떻게 해야하냐?
- 평상 시처럼 개발하되 테스트의 필요성을 느낄 때 점진적으로 TDD를 적용할 것을 권유

# 17.2 BDD
- TDD에서 파생된 소프트웨어 개발 방법론 (TDD + "사용자 행동")
- 사용자 행동을 "행동 명세"로 만들고 이를 바탕으로 테스트 코드를 작성하고 이에 맞춰 애플리케이션 설계

## TDD의 한계
- 무엇을, 어떻게 테스트해야 하는지 설명하지 않기 때문 -> 맥락 파악이 어려워짐
- 객체지향이 설계에서 보장되지 않음 (TDD와 객체지향은 무관함)
  - 한계라고 보기는 어렵지만 이러한 특징은 자바 개발자에게 아쉬움으로 느껴짐

## DDD (domain-driven development) 도메인 주도 개발
- 도메인 모델을 중심으로 비즈니스 요구사항을 이해하고 설계하는데 초점을 둠 -> 객체지향 추구 가능
- 하지만 소프트웨어 안정성과는 관련이 없음 (테스트에 대한 내용이 없음)

## TDD + DDD
- TDD : 안정성과 유연성을 확보, 하지만 객체지향 보장하지 않음
- DDD : 도메인 모델을 중심으로 설계할 수 있어서 객체지향설계 추구 가능, 하지만 소프트웨어 안정성을 확보하기 위함이 아님
**도메인 분석 단계에서 사용자 위주의 스토리를 만들고(DDD) + 이를 바탕으로 테스트 코드를 작성(TDD) -> BDD**

## BDD
- 개발자와 비개발자 사이의 협업을 강조
- 테스트 코드를 비개발자가 열람할 수 있도록 개발자와 비개발자 둘 다 이해할 수 있는 유비쿼터스 언어로 작성
- 공통된 언어를 바탕으로 요구사항 문서가 사용자 스토리 기반으로 작성돼야 함

### 행동 명세
- 어떤 상황에서(given), 어떤 행동을 할 때(when), 그러면(then) 어떤 일이 발생한다를 기술

## BDD의 핵심
1. 개발자와 비개발자 사이의 협업 (DDD의 특징)
2. 행동 명세(사용자 스토리 기반의 요구사항 작성)
3. 행동 명세의 테스트화 
4. 테스트의 문서화

# 이 책의 핵심
- 프로젝트의 상황에 맞는 해결책을 그때그때 선택하는 것이 중요하다.
