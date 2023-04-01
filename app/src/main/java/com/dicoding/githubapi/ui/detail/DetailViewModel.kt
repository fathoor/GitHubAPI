package com.dicoding.githubapi.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubapi.api.ApiConfig
import com.dicoding.githubapi.model.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _detail = MutableLiveData<DetailUserResponse>()
    val detail: MutableLiveData<DetailUserResponse> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    fun setDetailUser(detailUser: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(detailUser)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detail.value = response.body()
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}