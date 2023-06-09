package com.dicoding.githubapi.data.remote.retrofit

import com.dicoding.githubapi.data.remote.response.DetailUserResponse
import com.dicoding.githubapi.data.remote.response.ItemsItem
import com.dicoding.githubapi.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(@Query("q") query: String): Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}