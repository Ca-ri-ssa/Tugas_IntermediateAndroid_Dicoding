package com.carissa.intermediate.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.carissa.intermediate.data.response.ListStoryItem
import com.carissa.intermediate.data.retrofit.ApiService

class StoryPagingSource(
    private val apiService: ApiService,
    private val token: String
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllStories(token, page, params.loadSize)

            return if (response.isSuccessful && response.body()?.listStory != null) {
                val storyList = response.body()?.listStory
                val nextKey = if (storyList.isNullOrEmpty()) null else page + 1
                LoadResult.Page(
                    data = storyList ?: emptyList(),
                    prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception("Error to load story!"))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}