package ru.visionary.mixing.shiny_appearance.data.remote.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {
    @Multipart
    @POST("/api/v1/image/save")
    suspend fun saveImage(
        @Part image: MultipartBody.Part,
        @Part("protection") protection: RequestBody
    ): ResponseBody
}