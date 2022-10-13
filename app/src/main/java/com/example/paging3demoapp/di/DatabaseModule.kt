package com.example.paging3demoapp.di

import android.content.Context
import androidx.room.Room
import com.example.paging3demoapp.data.local.UnsplashDatabase
import com.example.paging3demoapp.utils.Constants.UNSPLASH_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):UnsplashDatabase{
        return Room.databaseBuilder(context,UnsplashDatabase::class.java,UNSPLASH_DATABASE).build()
    }
}