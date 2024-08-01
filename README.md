# android-map-notification

## 📄 프로그램 설명

`Retrofit` 라이브러리를 통해 카카오 로컬 API를 사용하여 키워드를 통해 장소를 검색할 수 있는 어플리케이션입니다.

또한 `Room` 라이브러리를 통해 사용자의 기기에 검색 기록이 저장되고, 검색 기록을 클릭하면 해당 검색 결과를 다시 확인할 수 있습니다.

이후 카카오 맵 SDK를 통해 검색된 장소의 위치를 확인할 수 있고, `Hilt`를 통하여 의존성 주입을 적용하였습니다.

이번 주차에는 `Firebase`를 통해 서버의 상태에 따라 Splash 화면을 구현하고, `FCM`을 사용하여 알림을 받을 수 있는 어플리케이션을 구현합니다.

## 🎯 1단계(Splash Screen) 구현할 기능

- 처음 앱 실행 시, Splash 화면 적용하기

    - `Firebase`의 `Remote Config`를 사용하여 서버의 상태 받아온다.

    - 받아온 정보가 정상(`ON_SERVICE`)인 경우, 지도 화면으로 넘어간다.

    - 받아온 정보가 정상(`ON_SERVICE`)이 아닌 경우, 메시지만 표시하고 지도 화면으로 넘어가지 않는다.

- 서버 상태 뿐만 아니라, UI 로딩을 고려하여 Splash 화면을 구현하기

- Splash 화면에서도 MVVM 아키텍처를 적용하여 구현하기

## 🎯 2단계(푸시 알림) 구현할 기능

- `Firebase`의 `FCM`을 사용하여 푸시 알림을 받기

    - 앱이 background에 있을 때, 푸시 알림을 받으면 기본적인 Notification을 띄운다.
  
    - 앱이 foreground에 있을 때, 푸시 알림을 받으면 커스텀 Notification을 띄워 알림을 받는다.

- 푸시 알림을 클릭하면 초기 화면으로 이동하기