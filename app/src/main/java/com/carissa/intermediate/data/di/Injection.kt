package com.carissa.intermediate.data.di

import android.content.Context
import com.carissa.intermediate.data.remotemediator.StoryDatabase
import com.carissa.intermediate.data.repository.StoryRepository
import com.carissa.intermediate.data.retrofit.ApiConfig

class Injection {
    companion object {
        fun provideRepository(context: Context): StoryRepository {
            val apiService = ApiConfig.getApiService()
            val storyDatabase = StoryDatabase.getDatabase(context)
            return StoryRepository(apiService, storyDatabase)
        }
    }
}