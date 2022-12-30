package com.pascal.myremovebackround.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pascal.myremovebackround.data.repo.RepositoryBg
import com.pascal.myremovebackround.model.ResponseBg
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ViewModelBg @Inject constructor(
    val repo: RepositoryBg
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    var getBg = MutableLiveData<ResponseBg>()
    var responseBg: LiveData<ResponseBg> = getBg
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Throwable>()

    fun getBgView(img: MultipartBody.Part) {
        isLoading.value = true
        repo.removeBg(img, compositeDisposable, {
            isLoading.value = false
            getBg.value = it
        }, {
            isLoading.value = false
            isError.value = it
        })
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}