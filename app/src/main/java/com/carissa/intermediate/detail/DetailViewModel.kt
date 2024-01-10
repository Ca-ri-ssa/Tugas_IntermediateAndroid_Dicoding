package com.carissa.intermediate.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carissa.intermediate.data.response.Story
import com.carissa.intermediate.data.response.StoryDetailResponse
import com.carissa.intermediate.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val storyDetailDataLive = MutableLiveData<Story>()
    fun getStoryDetailDataLive(): LiveData<Story> {
        return storyDetailDataLive
    }

    fun getStoryDetail(token: String, id: String) {
        val token = "Bearer $token"
        ApiConfig.getApiService().getDetailStory(token, id).enqueue(object : Callback<StoryDetailResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<StoryDetailResponse>,
                response: Response<StoryDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val storyDetail = response.body()?.story
                    storyDetailDataLive.value = storyDetail
                }
            }
            override fun onFailure(call: Call<StoryDetailResponse>, t: Throwable) {
                t.message?.let { Log.e("No Story Detail found", it)}
            }
        })
    }
}