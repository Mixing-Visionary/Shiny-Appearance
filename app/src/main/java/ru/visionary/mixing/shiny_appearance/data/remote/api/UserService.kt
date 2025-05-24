package ru.visionary.mixing.shiny_appearance.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse

interface UserService {
    @GET("/api/v1/user")
    suspend fun getCurrentUser(): Response<UserResponse>
}
