package com.dicoding.githubapi.ui.favorite

import androidx.lifecycle.ViewModel
import com.dicoding.githubapi.data.repository.FavoriteRepository
import com.dicoding.githubapi.data.local.entity.FavoriteEntity

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    fun showFavorite() = favoriteRepository.getFavorite()

    fun getUser(username: String) = favoriteRepository.getFavoriteUser(username)

    fun insertUser(favoriteEntity: FavoriteEntity) = favoriteRepository.insertFavorite(favoriteEntity)

    fun deleteUser(favoriteEntity: FavoriteEntity) = favoriteRepository.deleteFavorite(favoriteEntity)
}