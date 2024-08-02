# android-map-notification

## 프로젝트 설명
이 프로젝트는 카카오테크캠퍼스 2기 STEP2 6주차 미니과제로, Splash Screen과 푸시 알림 기능을 구현합니다.

## 구현 기능 목록
### 1단계 - Splash Screen
- Firebase의 Remote Config 설정
    - 서비스 상태를 나타내는 매개변수
      - `serviceState`
      - `serviceMessage`
- `serviceState` == `ON_SERVICE`
  - 초기 진입 화면에서 지도 화면으로 이동
- `serviceState` != `ON_SERVICE`
  - `serviceMessage` 값을 초기 진입 화면 하단에 표시 + 지도 화면으로 이동 X

### 2단계 - 푸시 알림
- Firebase의 Cloud Messaging 설정
- 테스트 메시지 전송
  - 백그라운드 상태 -> FCM 기본 값을 사용해 Notification 발생
  - 포그라운드 상태 -> Custom Notification 발생
- Notification 창 선택 시 초기 진입 화면 호출
