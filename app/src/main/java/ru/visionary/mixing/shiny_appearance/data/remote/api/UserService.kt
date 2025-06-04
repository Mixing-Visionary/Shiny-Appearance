package ru.visionary.mixing.shiny_appearance.data.remote.api

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse

interface UserService {
    @GET("/api/v1/user")
    suspend fun getCurrentUser(): Response<UserResponse>

    @DELETE("/api/v1/user")
    suspend fun deleteCurrentUser(): Response<Unit>

    @GET("/api/v1/user/{userId}")
    suspend fun getOtherUser(
        @Path("userId") userId: Int
    ): Response<UserResponse>

    @Multipart
    @PUT("/api/v1/user")
    suspend fun updateCurrentUser(
        @Part("nickname") nickname: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("password") password: RequestBody?
    ): Response<Unit>

}
