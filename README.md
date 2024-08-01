# android-map

- 카카오 테크 캠퍼스 과제(카카오맵) 수행을 위한 저장소입니다.

## content

- 실행 영상
  
![week6_1](https://github.com/user-attachments/assets/69e03c8f-851a-4d65-89fb-cb556d7b1d61)

## 푸시 알림

앱 실행 중 알림               백그라운드 알림

![week6_noti_fore](https://github.com/user-attachments/assets/ec18e78b-fe9e-490d-9a10-bc2f31aad907) ![week6_noti_back](https://github.com/user-attachments/assets/ae65f018-877d-438e-8067-ce8296d4c00b)

## 에러 화면

서비스 비활성화 상태 화면          인증 에러시 화면

![splash_error](https://github.com/user-attachments/assets/692f95a0-077e-484d-b840-85036e72252f) ![week4_1](https://github.com/user-attachments/assets/7c9bc6f0-c163-4058-83cc-1299120c7230)

## flow chart

![map_location](https://github.com/user-attachments/assets/05e64aed-bcc6-4a3f-9a08-9bb5d2377702)


## 프로젝트 구조도

![카카오맵_구조도](https://github.com/user-attachments/assets/b0330d76-cd25-419c-8823-ceae2a779571)

## feature

### 이전 단계

1. 검색어를 입력하면 검색 결과(15개 이상) 목록이 표시된다.
    - 리사이클러뷰 사용(세로 스크롤)

2. 입력한 검색어는 X를 눌러서 삭제할 수 있다.

3. 검색 결과 목록에서 하나의 항목을 선택할 수 있다.
    - 선택된 항목은 검색어 저장 목록에 추가된다.
    - 리사이클러뷰 사용(가로 스크롤)

4. 저장된 검색어는 X를 눌러서 삭제할 수 있다.

5. 이미 검색어 저장 목록에 있는 검색어를 검색 결과 목록에서 선택한 경우 기존 검색어는 삭제하고 다시 추가한다.

6. 저장된 검색어를 선택하면 해당 검색어의 검색 결과가 표시된다.

7. 검색 결과 목록 중 하나의 항목을 선택하면 해당 항목의 위치를 지도에 표시한다.

8. 앱 종료 시 마지막 위치를 저장하여 다시 앱 실행 시 해당 위치로 포커스 한다.

9. 카카오지도 onMapError() 호출 시 에러 화면을 보여준다.

### step1(Splash Screen)

1. 초기 진입 화면을 추가한다.

2. 매개변수 serviceState 값이 ON_SERVICE일 때만 초기 진입 화면이 지도 화면으로 넘어간다.

3. 매개변수 serviceState 값이 ON_SERVICE이 아닌 경우에는 serviceMessage 값을 초기 진입 화면 하단에 표시하고 지도 화면으로 진입하지 않는다.

### step2(푸시 알림)

1. Firebase Cloud Message로 테스트 메시지를 보낸다.
    - 앱이 백그라운드 상태일 경우 FCM 기본 값을 사용하여 Notification을 발생한다.
    - 앱이 포그라운드 상태일 경우 커스텀 Notification을 발생한다.

2. Notification 창을 터치하면 초기 진입 화면이 호출된다.
