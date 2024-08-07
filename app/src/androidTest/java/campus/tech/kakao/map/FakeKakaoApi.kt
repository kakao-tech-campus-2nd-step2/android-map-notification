package campus.tech.kakao.map

import campus.tech.kakao.map.data.net.KakaoApi
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import retrofit2.Response

class FakeKakaoApi : KakaoApi {
    override suspend fun getSearchKeyword(
        key: String,
        query: String,
        size: Int,
        page: Int
    ): Response<ResultSearchKeyword> {
        // 가짜 응답 데이터 생성
        val fakeDocuments = listOf(
            Place("1", "장소 1", "주소 1", "카테고리 1", "0.0", "0.0"),
            Place("2", "장소 2", "주소 2", "카테고리 2", "0.0", "0.0")
        )
        val responseBody = ResultSearchKeyword(documents = fakeDocuments)
        return Response.success(responseBody)
    }
}