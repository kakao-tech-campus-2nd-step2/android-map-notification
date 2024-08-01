# android-map-notification

## Step1

### 📜 Description

카카오맵 클론 코딩 _ 알림-파이어베이스


### 🎯 Tasks

- Main 기능 : **Splash Screen**

- 초기 진입 화면 추가
- Firebase의 Remote Config 설정
- 서비스 상태를 나타내는 매개변수 각각 등록
    - 매개변수 이름 : serviceState, serviceMessage
- 매개변수 serviceState 값이 ON_SERVICE일 때만 초기 진입 화면이 지도 화면으로 넘어감
- 매개변수 serviceState 값이 ON_SERVICE이 아닌 경우, serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않음
- 서버 상태, UI 로딩 등에 대한 상태 관리

---
## Step2

### 🎯 Tasks

- Main 기능 : **푸시 알림**

- Firebase Cloud Message 설정
- 테스트 메시지
    - 앱이 백그라운드 상태일 경우 FCM 기본 값을 사용하여 Notification 발생
    - 앱이 포그라운드 상태일 경우 커스텀 Notification 발생
- Notification 창을 터치하면 초기 진입 화면 호출