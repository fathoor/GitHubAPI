package com.dicoding.githubapi.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubapi.api.ApiConfig
import com.dicoding.githubapi.model.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    private val _listFollower = MutableLiveData<List<ItemsItem>>()
    val listFollower: MutableLiveData<List<ItemsItem>> = _listFollower

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: MutableLiveData<List<ItemsItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    fun getUserFollower(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _listFollower.value = response.body()
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isError.value = true
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _listFollowing.value = response.body()
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isError.value = true
            }
        })
    }
}