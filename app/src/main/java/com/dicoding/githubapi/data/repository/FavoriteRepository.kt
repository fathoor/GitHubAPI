package com.dicoding.githubapi.data.repository

import com.dicoding.githubapi.data.local.entity.FavoriteEntity
import com.dicoding.githubapi.data.local.room.FavoriteDao
import com.dicoding.githubapi.utils.AppExecutors

class FavoriteRepository private constructor(
    private val favoriteDao: FavoriteDao,
    private val appExecutors: AppExecutors
) {
    fun getFavorite() = favoriteDao.getFavorite()

    fun getFavoriteUser(username: String) = favoriteDao.getFavoriteUser(username)

    fun insertFavorite(favoriteEntity: FavoriteEntity) {
        appExecutors.diskIO.execute { favoriteDao.insertFavorite(favoriteEntity) }
    }

    fun deleteFavorite(favoriteEntity: FavoriteEntity) {
        appExecutors.diskIO.execute { favoriteDao.deleteFavorite(favoriteEntity) }
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(favoriteDao: FavoriteDao, appExecutors: AppExecutors): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(favoriteDao, appExecutors)
            }.also { instance = it }
    }
}