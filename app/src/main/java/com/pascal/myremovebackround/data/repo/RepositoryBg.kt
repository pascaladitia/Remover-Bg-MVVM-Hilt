package com.pascal.myremovebackround.data.repo

import com.pascal.myremovebackround.data.api.ApiService
import com.pascal.myremovebackround.model.ResponseBg
import com.pascal.myremovebackround.utils.showLog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class RepositoryBg @Inject constructor(
    val apiService: ApiService
) {
    fun removeBg(
        img: MultipartBody.Part,
        compositeDisposable: CompositeDisposable,
        responHandler: (ResponseBg) -> Unit,
        errorHandler: (Throwable) -> Unit
    ): Disposable {
        return apiService.getBg(img)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showLog("tag repo", it.toString())
                responHandler(it)
            }, {
                showLog("tag error", it.toString())
                errorHandler(it)
            }).also {
                compositeDisposable.add(it)
            }
    }
}