package com.yhezra.githubapp.data.remote.api

import com.yhezra.githubapp.data.remote.model.DetailUser
import com.yhezra.githubapp.data.remote.model.SearchUsersResponse
import com.yhezra.githubapp.data.remote.model.UserItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("users")
    fun getListUser(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Call<List<UserItem>>

    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getSearchUser(
        @Query("q") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<SearchUsersResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUser>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getListFollowingUser(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<UserItem>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getListFollowersUser(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<UserItem>>
}