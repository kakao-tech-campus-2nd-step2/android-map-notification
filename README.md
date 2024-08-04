# android-map-notification

## 1단계 - Splash Screen
### 기능 요구 사항
- 초기 진입 화면을 추가한다.
- Firebase의 Remote Config를 설정한다.
  - 서비스 상태를 나타내는 매개변수를 아래와 같이 각각 등록한다.
    - 매개변수 이름:serviceState, serviceMessage
- 매개변수 serviceState 값이 ON_SERVICE일 때만 초기 진입 화면이 지도 화면으로 넘어간다.
- 매개변수 serviceState 값이 ON_SERVICE이 아닌 경우에는 serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않는다.
### 프로그래밍 요구 사항
- 서버 상태, UI 로딩 등에 대한 상태 관리를 한다.
- 새로 추가되는 부분에도 MVVM 아키텍처 패턴을 적용한다.
- 코드 컨벤션을 준수하며 프로그래밍한다.

## 2단계 - 푸시 알림
### 기능 요구 사항
- Firebase Cloud Message를 설정한다.
- 테스트 메시지를 보낸다.
  - 앱이 백그라운드 상태일 경우 FCM 기본 값을 사용하여 Notification을 발생한다.
  - 앱이 포그라운드 상태일 경우 커스텀 Notification을 발생한다.
- Notification 창을 터치하면 초기 진입 화면이 호출된다.
### 프로그래밍 요구 사항
- 코드 컨벤션을 준수하며 프로그래밍한다.