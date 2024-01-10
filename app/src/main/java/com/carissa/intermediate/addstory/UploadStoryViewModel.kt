package com.carissa.intermediate.addstory

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carissa.intermediate.data.response.AddStoryResponse
import com.carissa.intermediate.data.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadStoryViewModel(private val context: Context): ViewModel() {
    fun uploadFileStory(token: String, imageFile: File, description: String) {
        val token = "Bearer $token"
        val requestDesc = description.toRequestBody("text/plain".toMediaType())
        val requestImg = imageFile.asRequestBody("image/*".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("photo", imageFile.name, requestImg)

        ApiConfig.getApiService().uploadStory(token, imagePart, requestDesc)
            .enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                    if (response.isSuccessful) {
                        Log.d("Success", "Upload succeed")
                        (context as UploadStoryActivity).finish()
                    }
                }
                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    Log.d("Failed", "Upload failed")
                    (context as UploadStoryActivity).finish()
                }
            })
    }
}

class UploadStoryViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadStoryViewModel::class.java)) {
            return UploadStoryViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}