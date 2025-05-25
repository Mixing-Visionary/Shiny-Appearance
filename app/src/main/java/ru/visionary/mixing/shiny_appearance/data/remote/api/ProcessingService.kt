package ru.visionary.mixing.shiny_appearance.data.remote.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.visionary.mixing.shiny_appearance.domain.model.ProcessingResponse

interface ProcessingService {
    @Multipart
    @POST("/api/v1/processing")
    suspend fun processImage(
        @Part image: MultipartBody.Part,
        @Query("style") style: String,
        @Query("strength") strength: Float? = null
    ): Response<ProcessingResponse>
}