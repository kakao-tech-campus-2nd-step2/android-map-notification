# Android Map 클론 코딩
---
![제목을-입력해주세요_-001](https://github.com/user-attachments/assets/5d644246-a929-49eb-9dfe-d160fb270568)


## 🍪프로젝트 소개
---
카카오테크캠퍼스 2기 STEP2를 진행하면서 코틀린을 활용하여 지도 맵 클론 코딩을 진행하였습니다.
"영주의 맵"은 카카오 지도 API 와 카카오 로컬 API를 활용하여 지도를 표시하고 장소를 검색할 수 있는 어플리케이션입니다.

## 🗓 개발 기간
---
- 2024.06.24 ~ 2024.08.02
- 1주차 : 앱 기능 분석
- 2주차 : RecyclerView, SQLite 활용하여 검색 기능 구현
- 3주차 : 카카오 로컬 API 활용하여 검색 기능 구현(Retrofit) , 카카오 지도 API 활용하여 지도 표시
- 4주차 : 지도에 위치 마커 표시 및 최근 검색어 저장 기능 추가 & UI 테스트 코드 작성
- 5주차 : Room, Hilt(의존성 주입) 를 활용하여 MVVM 구조로 리팩터링
- 6주차 : 스플래시 스크린 추가, FCM 푸시 알림, Splash 뷰모델 유닛테스트 코드 작성


## 📁 프로젝트 구조
<img width="300" alt="스크린샷 2024-08-02 오후 2 16 40" src="https://github.com/user-attachments/assets/e41c4e37-85f5-4dc8-b594-c228d4bdfc39">


## 🗂 사용 라이브러리
---

| 라이브러리                                              | 버전     | 설명                                           |
|---------------------------------------------------------|----------|------------------------------------------------|
| `com.google.dagger:hilt-android`                        | 2.48.1   | Hilt 의존성 주입 라이브러리                      |
| `com.kakao.maps.open:android`                           | 2.9.5    | Kakao 지도 API                                  |
| `com.squareup.retrofit2:retrofit`                       | 2.11.0   | Retrofit HTTP 클라이언트 라이브러리               |
| `com.squareup.retrofit2:converter-gson`                 | 2.11.0   | Gson 변환기                                     |
| `com.google.firebase:firebase-bom`                      | 33.1.2   | Firebase BOM (Bill of Materials)                |
| `com.google.firebase:firebase-config-ktx`               | 22.0.0   | Firebase Remote Config KTX 라이브러리            |
| `com.google.firebase:firebase-messaging-ktx`            | 24.0.0   | Firebase Cloud Messaging KTX 라이브러리          |
| `androidx.room:room-runtime`                            | 2.6.1    | Room 데이터베이스 런타임 라이브러리              |
| `androidx.room:room-ktx`                                | 2.6.1    | Room KTX 확장 라이브러리                         |
| `androidx.room:room-testing`                            | 2.6.1    | Room 테스팅 라이브러리                           |
| `androidx.lifecycle:lifecycle-viewmodel-ktx`            | 2.8.3    | ViewModel KTX 확장 라이브러리                     |
| `io.mockk:mockk-android`                                | 1.13.11  | MockK Android 테스팅 라이브러리                  |
| `io.mockk:mockk-agent`                                  | 1.13.11  | MockK 에이전트                                   |
| `org.robolectric:robolectric`                           | 4.11.1   | Robolectric 테스팅 라이브러리                     |
| `org.jetbrains.kotlinx:kotlinx-coroutines-test`         | 1.7.3    | Kotlinx Coroutines 테스팅 라이브러리              |
| `androidx.test.espresso:espresso-core`                  | 3.6.1    | Espresso 코어 테스팅 라이브러리                   |
| `androidx.test.espresso:espresso-contrib`               | 3.6.1    | Espresso 테스팅 라이브러리                        |
| `androidx.test.espresso:espresso-intents`               | 3.6.1    | Espresso Intents 테스팅 라이브러리                |
| `androidx.test:rules`                                   | 1.6.1    | AndroidX 테스트 룰                                |
| `androidx.test:core-ktx`                                | 1.6.1    | Android 테스트 코어 KTX 라이브러리                 |
| `androidx.arch.core:core-testing`                       | 2.2.0    | Architecture Components Core Testing            |
| `androidx.test.ext:junit`                               | 1.2.1    | AndroidX JUnit 확장 라이브러리                    |
| `junit:junit`                                           | 4.13.2   | JUnit 테스트 라이브러리                           |
| `com.google.dagger:hilt-android-testing`                | 2.48.1   | Hilt Android 테스팅 라이브러리                    |


