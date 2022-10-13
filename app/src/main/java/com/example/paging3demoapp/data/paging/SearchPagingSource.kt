package com.example.paging3demoapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3demoapp.data.remote.UnsplashApi
import com.example.paging3demoapp.model.UnsplashedImage
import com.example.paging3demoapp.utils.Constants.ITEMS_PER_PAGE
import javax.inject.Inject

class SearchPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashedImage>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashedImage>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashedImage> {
        val currentPage = params.key ?: 1
        return try{
            val response = unsplashApi.searchImages(query = query, per_page = ITEMS_PER_PAGE)
            val endOfPaginationReached = response.images.isEmpty()
            if (response.images.isNotEmpty()){
                LoadResult.Page(
                    data = response.images,
                    prevKey = if (currentPage == 1) null else currentPage -1,
                    nextKey  = if (endOfPaginationReached) null else currentPage +1
                )
            }else{
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

        }catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}