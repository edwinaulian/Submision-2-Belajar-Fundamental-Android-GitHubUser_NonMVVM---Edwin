package com.example.githubapi

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getListUsers(): Call<ArrayList<UsersResponse>>

    @GET("search/users")
    fun getSearchUser(@Query("q") q: String): Call<UserSearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<ListFollowersResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<ListFollowersandFollowingResponse>>
}