package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//class ViewModelFactory(private val context: Context
//                       , private val retrofitRepository: RetrofitRepository
//                       , private val searchHistoryRepository: SearchHistoryRepository
//)
//    : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
//            return SearchViewModel(context, retrofitRepository, searchHistoryRepository) as T
//        }
//        else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            return MainViewModel(searchHistoryRepository) as T
//        }
//        else {
//            throw IllegalArgumentException("Failed to create ViewModel : ${modelClass.name}")
//        }
//    }
//}
