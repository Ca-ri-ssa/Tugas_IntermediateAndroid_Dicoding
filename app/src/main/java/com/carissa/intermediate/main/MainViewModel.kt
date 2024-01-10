package com.carissa.intermediate.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.carissa.intermediate.data.di.Injection
import com.carissa.intermediate.data.repository.StoryRepository
import com.carissa.intermediate.data.response.ListStoryItem


class MainViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getAllStoryPaging(token: String): LiveData<PagingData<ListStoryItem>> = repository.getAllStory(token).cachedIn(viewModelScope)
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}