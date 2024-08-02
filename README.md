# android-map-notification

### 구현할 기능 목록
[step1]
- 서비스 상태를 나타내는 매개변수를 Firebase의 Remote Config에 등록
- serviceState 값이 ON_SERVICE일 때만 초기 진입 화면이 지도 화면으로 넘어감
- serviceState 값이 ON_SERVICE이 아닌 경우에는 serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않음

[step2]
- 알림 권한 설정
- Firebase Cloud Message 설정
- 앱이 백그라운드 상태일 경우 FCM 기본 값을 사용하여 Notification을 발생
- 앱이 포그라운드 상태일 경우 커스텀 Notification을 발생
- Notification 창을 터치하면 초기 진입 화면이 호출됨