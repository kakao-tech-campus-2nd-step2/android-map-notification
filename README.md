# android-map-notification

### KakaoTechCampus 2기 Step2 - 6주차 과제 : notification

### step 1 - spalsh screen

### 기능 구현 사항
- [ ] remote config 설정
- [ ] 초기 진입 화면 activity 구현
- [ ] 초기 진입 화면 구현
- [ ] mvvm 패턴 적용 구현 

### 앱 작동 순서
1. 앱 실행 시 'splash activity' 가 실행된다.
2. spalshViewModel로 Remote Config의 serviceState와 serviceMessage를 불러온다.
3. serviceState == ON_SERVICE인 경우 MainActivity로 넘어가 기존의 앱의 지도화면이 뜨며 앱이 실행된다.
4. serviceState != ON_SERVICE인 경우 serviceMessage를 표시하고 해당 화면에서 작동이 끝난다.

