package com.carissa.intermediate.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.carissa.intermediate.data.remotemediator.StoryDatabase
import com.carissa.intermediate.data.remotemediator.StoryRemoteMediator
import com.carissa.intermediate.data.response.ListStoryItem
import com.carissa.intermediate.data.retrofit.ApiService


class StoryRepository(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {
    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}