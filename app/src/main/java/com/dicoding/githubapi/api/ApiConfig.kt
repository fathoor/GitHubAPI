package com.dicoding.githubapi.api

import com.dicoding.githubapi.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val API_KEY = BuildConfig.API_KEY

        fun getApiService(): ApiService {
            val authInterceptor = Interceptor { chain ->
                val request = chain.request()
                val requestHeader = request.newBuilder()
                    .addHeader("Authorization", API_KEY)
                    .build()
                chain.proceed(requestHeader)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}