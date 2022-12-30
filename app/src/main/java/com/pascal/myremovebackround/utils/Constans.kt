package com.pascal.myremovebackround.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast

class Constans {

    companion object {
        const val BASE_URL = "https://api.remove.bg/v1.0/"
    }
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun showLog(tag: String, msg: String) {
    Log.e(tag, msg)
}

fun visibility(view: View, boolean: Boolean) {
    if (boolean == true) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

fun setImagefromBase64(img: String): Bitmap {
    val imageBytes = Base64.decode(img, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    return decodedImage
}

fun Activity.initPermissionStorage() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
        )
    }
}