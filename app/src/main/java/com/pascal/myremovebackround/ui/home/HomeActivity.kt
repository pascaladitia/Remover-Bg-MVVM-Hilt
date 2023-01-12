package com.pascal.myremovebackround.ui.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.acs.dial.utils.FilePath
import com.pascal.myremovebackround.data.viewModel.ViewModelBg
import com.pascal.myremovebackround.databinding.ActivityHomeBinding
import com.pascal.myremovebackround.databinding.DialogImageFullscreenBinding
import com.pascal.myremovebackround.utils.initPermissionStorage
import com.pascal.myremovebackround.utils.setImagefromBase64
import com.pascal.myremovebackround.utils.showToast
import com.pascal.myremovebackround.utils.visibility
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: ViewModelBg by viewModels()
    private var stringBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Remove BG!"

        initView()
        initPermissionStorage()
    }

    private fun initView() {
        viewModel.responseBg.observe(this, Observer {
            visibility(binding.imgResult, true)
            showToast("Hold your image for save!")
            stringBase64 = it.data?.resultB64.toString()

            binding.imgResult.setImageBitmap(setImagefromBase64(stringBase64!!))
        })

        viewModel.isLoading.observe(this, Observer {
            if (it == true) {
                visibility(binding.imgIcon, false)
                visibility(binding.imgResult, false)
                visibility(binding.loading, true)
            } else {
                visibility(binding.loading, false)
            }
        })

        viewModel.isError.observe(this, Observer {
          showToast("Error, please try again")
            visibility(binding.imgIcon, true)
        })

        binding.btnUpload.setOnClickListener {
            if (binding.checkbox.isChecked) {
                getImage()
            } else showToast("Please read and agree privacy police")
        }

        binding.card.setOnClickListener {
            if (stringBase64 != null) {
                fullImage()
            } else {
                showToast("upload your image")
            }
        }

        binding.card.setOnLongClickListener {
            if (stringBase64 != null) {
                saveImage(setImagefromBase64(stringBase64!!))
            } else {
                showToast("upload your image")
            }
         true
        }

        binding.btnPrivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyActivity::class.java))
        }
    }

    private fun fullImage() {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val bind: DialogImageFullscreenBinding = DialogImageFullscreenBinding.inflate(layoutInflater)
        dialog.setContentView(bind.root)
        dialog.setCancelable(true)

        bind.fullImage.setImageBitmap(setImagefromBase64(stringBase64!!))

        dialog.show()
    }

    private fun getImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                val image_path = fileUri.let { FilePath.getPath(this, it) }

                uploadFile(image_path)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
        }

    private fun uploadFile(imagePath: String?) {
        val file = File(imagePath)
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image_file", file.name, requestFile)

        viewModel.getBgView(body)
    }

    private fun saveImage(image: Bitmap) {
        try {
            val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())

            val root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).toString()
            val myDir = File("$root/saved_images")
            myDir.mkdirs()
            val fname = timeStamp + ".png"
            val file = File(myDir, fname)
            val out = FileOutputStream(file)
            val bm: Bitmap = image
            bm.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

            showToast("Image Saving...")
        } catch (e: Exception) {
            Log.d("onBtnSavePng", e.toString())
        }

    }

}