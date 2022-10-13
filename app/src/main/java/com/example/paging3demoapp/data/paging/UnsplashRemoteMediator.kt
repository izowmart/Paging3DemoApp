package com.example.paging3demoapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging3demoapp.data.local.UnsplashDatabase
import com.example.paging3demoapp.data.remote.UnsplashApi
import com.example.paging3demoapp.model.UnsplashRemoteKeys
import com.example.paging3demoapp.model.UnsplashedImage
import com.example.paging3demoapp.utils.Constants.ITEMS_PER_PAGE
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class UnsplashRemoteMediator(
    private val database: UnsplashDatabase,
    private val unsplashApi: UnsplashApi
): RemoteMediator<Int, UnsplashedImage>() {

    private val unsplashRemoteKeysDao = database.unsplashRemoteKeysDao()
    private val unsplashImageDao = database.unsplashImageDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashedImage>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }

            val response = unsplashApi.getAllImages(page = currentPage, per_page = ITEMS_PER_PAGE)
            Timber.d("currentPage $currentPage")
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

                database.withTransaction { // Retrieves data sequentially
                    if (loadType == LoadType.REFRESH) {
                        // We clear all the data in our database to give room for new ones
                        unsplashImageDao.deleteAllImages()
                        unsplashRemoteKeysDao.deleteAllRemoteKeys()
                    }
                    val keys = response.map { unsplashImage ->
                        UnsplashRemoteKeys(
                            id = unsplashImage.id,
                            prevPage = prevPage,
                            nextPage = nextPage
                        )
                    }

                    unsplashImageDao.addImages( images = response)
                    unsplashRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UnsplashedImage>): UnsplashRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { unsplashedImage ->
            unsplashRemoteKeysDao.getRemoteKeys(id = unsplashedImage.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UnsplashedImage>): UnsplashRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashedImage ->
                unsplashRemoteKeysDao.getRemoteKeys(id = unsplashedImage.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UnsplashedImage>): UnsplashRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                unsplashRemoteKeysDao.getRemoteKeys(id = id)
            }
        }

    }

}