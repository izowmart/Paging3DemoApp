package com.example.paging3demoapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3demoapp.model.UnsplashedImage

@Dao
interface UnsplashImageDao {

    @Query("SELECT * FROM unsplash_image_table" )
    fun getAllImages():PagingSource<Int, UnsplashedImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImages(images:List<UnsplashedImage>)

    @Query("DELETE FROM unsplash_image_table")
    suspend fun deleteAllImages()
}