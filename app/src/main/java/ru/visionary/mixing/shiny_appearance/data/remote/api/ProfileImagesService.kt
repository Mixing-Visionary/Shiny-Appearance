package ru.visionary.mixing.shiny_appearance.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.visionary.mixing.shiny_appearance.domain.model.ProfileImagesResponse

interface ProfileImagesService {
    @GET("/api/v1/user/images")
    suspend fun getUserImages(
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("protection") protection: String
    ): Response<ProfileImagesResponse>

    @GET("/api/v1/user/{userId}/images")
    suspend fun getOtherUserImages(
        @Path("userId") userId: Int,
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("protection") protection: String
    ): Response<ProfileImagesResponse>
}