package ru.visionary.mixing.shiny_appearance.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse

interface UserService {
    @GET("/api/v1/user")
    suspend fun getCurrentUser(): Response<UserResponse>

    @GET("/api/v1/user/{userId}")
    suspend fun getOtherUser(
        @Path("userId") userId: Int
    ): Response<UserResponse>
}
