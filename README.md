# android-map-notification

## Step1
- 초기 진입 화면 Activity 만들기
- 초기 진입 화면 Layout 만들기
- Firebase Remote Config 설정
- serviceState, serviceMessage 매개변수 등록
- 매개변수 serviceState 값이 ON_SERVICE일 때만 초기 진입 화면이 지도 화면으로 넘어감
- 매개변수 serviceState 값이 ON_SERVICE이 아닌 경우에는 serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않음
- MVVM 패턴 적용