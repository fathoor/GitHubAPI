package com.dicoding.githubapi.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubapi.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite ORDER BY username ASC")
    fun getFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite WHERE username = :username")
    fun getFavoriteUser(username: String): LiveData<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    fun deleteFavorite(favorite: FavoriteEntity)
}