package com.yhezra.githubapp.api

import com.yhezra.githubapp.model.DetailUser
import com.yhezra.githubapp.model.SearchUsersResponse
import com.yhezra.githubapp.model.UserItem
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