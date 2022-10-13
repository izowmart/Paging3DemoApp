package com.example.paging3demoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paging3demoapp.data.local.dao.UnsplashImageDao
import com.example.paging3demoapp.data.local.dao.UnsplashRemoteKeysDao
import com.example.paging3demoapp.model.UnsplashRemoteKeys
import com.example.paging3demoapp.model.UnsplashedImage

@Database(entities = [UnsplashedImage::class, UnsplashRemoteKeys::class], version = 1)
abstract class UnsplashDatabase: RoomDatabase() {
     abstract fun unsplashImageDao() : UnsplashImageDao
     abstract fun unsplashRemoteKeysDao() : UnsplashRemoteKeysDao
}