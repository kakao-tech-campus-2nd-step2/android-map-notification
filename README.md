# android-map-notification
## Step 1. Splash Screen
### 구현 기능 목록

1. 초기 진입 화면 추가
2. Firebase의 Remote Config 설정
   - 서비스 상태 나타내는 매개변수 각각 등록
     - 매개변수 이름: serviceState, serviceMessage
3. 매개변수 serviceState 값 = ON_SERVICE
   - 초기 진입 화면 -> 지도 화면
4. 매개변수 serviceState 값 != ON_SERVICE
   - 지도 화면 진입 X
   - serviceMessage 값 -> 초기 진입 화면 하단에 표시
5. 상태 관리
   - 서버 상태
   - UI 로딩
6. MVVM 아키텍처 패턴 적용