package campus.tech.kakao.map.presenter.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.base.ErrorEnum


class ErrorViewModel : ViewModel() {
    private val _type = MutableLiveData<ErrorEnum>()
    val type: LiveData<ErrorEnum> = _type
    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    fun setErrorData(type: ErrorEnum, msg: String) {
        _type.value = type
        _msg.value = msg
    }

}