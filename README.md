# 맵 위치 검색 안드로이드 앱
- 카테캠 Step2 Week6
- 장소 검색 및 장소 저장이 가능한 안드로이드 앱

## 기능
**1. 초기 진입 화면 추가** 
- firebase의 Remote Config 설정
- serviceState 값이 ON_SERVICE 일 때 
  - 지도 화면으로 진입
- serviceState 값이 ON_SERVICE가 아닐 때 
  - serviceMessage를 초기 진입 화면 하단에 표시
  - 지도 화면으로 진입하지 않음

**2. Notification 발생**
- 앱이 백그라운드 상태일 때 FCM 기본 값으로 Notification 발생
- 앱이 포그라운드 상태일 때 커스텀 Notification 발생


## 실행
- SplashActivity.kt 에서 시작

## 환경
- Kotlin
- Android Studio