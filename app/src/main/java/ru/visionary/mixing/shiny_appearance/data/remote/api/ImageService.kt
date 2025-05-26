package ru.visionary.mixing.shiny_appearance.data.remote.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import ru.visionary.mixing.shiny_appearance.domain.model.ImageResponse
import ru.visionary.mixing.shiny_appearance.domain.model.UpdateImageRequest

interface ImageService {
    @Multipart
    @POST("/api/v1/image/save")
    suspend fun saveImage(
        @Part image: MultipartBody.Part,
        @Part("protection") protection: RequestBody
    ): ResponseBody

    @GET("/api/v1/image/{uuid}")
    suspend fun getImageByUuid(
        @Path("uuid") uuid: String
    ): Response<ImageResponse>

    @PUT("/api/v1/image/{uuid}")
    suspend fun updateImagePrivacy(
        @Path("uuid") uuid: String,
        @Body request: UpdateImageRequest
    ): Response<Unit>

}