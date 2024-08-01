# android-map-notification
## 1단계 요구 사항
- [x] 초기 진입 화면 구현
- [x] serviceState값이 ON_SERVICE 인 경우 지도 화면으로 전환
- [x] serviceState값이 ON_SERVICE가 아닌 경우 serviceMessage 값을 화면에 표시하고 유지
## 2단계 요구 사항
- [x] FCM 설정
- [ ] 테스트 메시지 수신하기
  - [x] 백그라운드에서 FCM 기본값 알림 발생
  - [ ] 포그라운드에서 커스텀 알림 발생
- [ ] 알림을 터치하면 초기화면 진입
- [x] 알림 권한 요청하기