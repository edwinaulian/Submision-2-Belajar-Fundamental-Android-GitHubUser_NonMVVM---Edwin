package com.example.githubapi

import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("users")
    fun getListUsers(): Call<ArrayList<UsersResponse>>

    @GET("search/users")
    fun getSearchUser(@Query("q") q: String ): Call<UserSearchResponse>

}