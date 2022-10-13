package com.example.paging3demoapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3demoapp.data.local.UnsplashDatabase
import com.example.paging3demoapp.data.paging.SearchPagingSource
import com.example.paging3demoapp.data.paging.UnsplashRemoteMediator
import com.example.paging3demoapp.data.remote.UnsplashApi
import com.example.paging3demoapp.model.UnsplashedImage
import com.example.paging3demoapp.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class Repository @Inject constructor(
    private val unsplashDatabase: UnsplashDatabase,
    private val unsplashApi: UnsplashApi
) {

    fun getAllImages(): Flow<PagingData<UnsplashedImage>>{
        val pagingSourceFactory = {unsplashDatabase.unsplashImageDao().getAllImages()}
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = UnsplashRemoteMediator(
                database = unsplashDatabase,
                unsplashApi = unsplashApi
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun searchImages(query: String): Flow<PagingData<UnsplashedImage>>{
       return Pager(
           config  = PagingConfig(pageSize = ITEMS_PER_PAGE),
           pagingSourceFactory = { SearchPagingSource(unsplashApi = unsplashApi, query = query) }
       ).flow
    }

}