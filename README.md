# android-map-notification
## 기능 요구 사항
- 초기 진입 화면을 추가한다. 
- Firebase의 Remote Config를 설정한다.
- 서비스 상태를 나타내는 매개변수를 아래와 같이 각각 등록한다.
  - 매개변수 이름:serviceState, serviceMessage
  - 매개변수 serviceState 값이 ON_SERVICE일 때만 초기 진입 화면이 지도 화면으로 넘어간다.
  - 매개변수 serviceState 값이 ON_SERVICE이 아닌 경우에는 serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않는다.
