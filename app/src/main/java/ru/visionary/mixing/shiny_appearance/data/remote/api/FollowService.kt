package ru.visionary.mixing.shiny_appearance.data.remote.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.visionary.mixing.shiny_appearance.domain.model.UsersResponse

interface FollowService {
    @POST("/api/v1/user/{userId}/follow")
    suspend fun followUser(@Path("userId") userId: Int): Response<Unit>

    @DELETE("/api/v1/user/{userId}/follow")
    suspend fun followUserDelete(@Path("userId") userId: Int): Response<Unit>

    @GET("/api/v1/user/follows")
    suspend fun getFollows(
        @Query("size") size: Int,
        @Query("page") page: Int
    ): Response<UsersResponse>

    @GET("/api/v1/user/{userId}/follows")
    suspend fun getFollowsById(
        @Path("userId") uuid: Int,
        @Query("size") size: Int,
        @Query("page") page: Int
    ): Response<UsersResponse>

    @GET("/api/v1/user/followers")
    suspend fun getFollowers(
        @Query("size") size: Int,
        @Query("page") page: Int
    ): Response<UsersResponse>
}
