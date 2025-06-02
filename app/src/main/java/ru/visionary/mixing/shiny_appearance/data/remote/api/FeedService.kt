package ru.visionary.mixing.shiny_appearance.data.remote.api


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.visionary.mixing.shiny_appearance.domain.model.ProfileImagesResponse

interface FeedService {

    @GET("/api/v1/feed")
    suspend fun getFeed(
        @Query("sort") sort: String,
        @Query("size") size: Int,
        @Query("page") page: Int
    ): Response<ProfileImagesResponse>
}
