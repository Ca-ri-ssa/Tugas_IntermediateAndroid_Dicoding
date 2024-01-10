package com.carissa.intermediate.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.carissa.intermediate.data.response.ListStoryItem
import com.carissa.intermediate.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class MapsViewModel(private val context: Context) : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _storyListDataLive = MutableLiveData<List<ListStoryItem>>()
    val storyListDataLive: LiveData<List<ListStoryItem>> = _storyListDataLive

    fun getStoryLocation(token: String) {
        val token = "Bearer $token"
        viewModelScope.launch {
            try {
                val response = apiService.getAllStoryLocation(token)

                if (response.isSuccessful) {
                    val stories = response.body()?.listStory
                    _storyListDataLive.value = stories ?: emptyList()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Error", it) }
            }
        }
    }
}

class MapsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}