# android-map-notification

## 🙋‍♀️ 개요
사용자가 검색어를 입력하면 검색 결과를 보여주고 이 중 하나의 항목을 선택하면 해당 항목의 위치를 지도에 표시한다. 
앱 종료 시 마지막 위치를 저장하여 다시 실행 시 해당 위치로 포커스한다.

## ✨ 주요기능
1. 1단계
    - 초기 진입 화면을 추가한다.
    - Firebase의 Remote_Config 설정
      - 매개변수 serviceState == ON_SERVICE) 초기 진입 화면이 지도 화면으로 넘어간다.
      - 매개변수 serviceState != ON_SERVICE) serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않는다.
2. 2단계
   - Firebase Cloud Message를 설정한다.
     - 앱 -> 백그라운드 상태) **FCM 기본 값**을 사용하여 Notification을 발생한다
     - 앱 -> 포그라운드 상태) **커스텀 Notification**을 발생한다.
   - Notification 창을 터치하면 초기 진입 화면이 호출된다.