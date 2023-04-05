package com.dicoding.githubapi.di

import android.content.Context
import com.dicoding.githubapi.data.repository.FavoriteRepository
import com.dicoding.githubapi.data.local.room.FavoriteDatabase
import com.dicoding.githubapi.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        val appExecutors = AppExecutors()
        return FavoriteRepository.getInstance(dao, appExecutors)
    }
}