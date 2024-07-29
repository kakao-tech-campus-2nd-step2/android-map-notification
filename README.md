# android-map-notification
**카카오 테크 캠퍼스 STEP2 Android 6주차 미션 - 알림**
## 기능 요구사항
### 1단계 기능
- 초기 진입 화면을 추가한다.
- Firebase의 Remote Config를 설정한다.
  - 서비스 상태를 나타내는 매개변수를 아래와 같이 각각 등록한다.
    - 매개변수 이름:serviceState, serviceMessage
- 매개변수 serviceState 값이 ON_SERVICE일 때만 초기 진입 화면이 지도 화면으로 넘어간다.
- 매개변수 serviceState 값이 ON_SERVICE이 아닌 경우에는 serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않는다.
## 프로그래밍 요구사항
### 1단계 요구사항
- 서버 상태, UI 로딩 등에 대한 상태 관리를 한다.
- 새로 추가되는 부분에도 MVVM 아키텍처 패턴을 적용한다.
- 코드 컨벤션을 준수하며 프로그래밍한다.
## 구현할 기능 목록
### 1단계 기능
- [ ] 스플래시 화면 구현
- [ ] Remote Config 값 불러와 비교하기
- [ ] 에러시 serviceMessage 출력
