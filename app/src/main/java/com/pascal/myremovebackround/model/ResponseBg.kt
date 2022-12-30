package com.pascal.myremovebackround.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseBg(

	@field:SerializedName("data")
	val data: Data? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("foreground_top")
	val foregroundTop: Int? = null,

	@field:SerializedName("foreground_left")
	val foregroundLeft: Int? = null,

	@field:SerializedName("result_b64")
	val resultB64: String? = null,

	@field:SerializedName("foreground_height")
	val foregroundHeight: Int? = null,

	@field:SerializedName("foreground_width")
	val foregroundWidth: Int? = null
) : Parcelable
