package com.pascal.myremovebackround.data.api

import com.pascal.myremovebackround.model.ResponseBg
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @Headers("Accept: application/json",
        "X-Api-Key: REPLACE_YOU_TOKEN"
    )
    @POST("removebg")
    fun getBg(@Part userReq: MultipartBody.Part): Single<ResponseBg>
}