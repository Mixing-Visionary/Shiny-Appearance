package ru.visionary.mixing.shiny_appearance.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.visionary.mixing.shiny_appearance.domain.model.AuthResponse
import ru.visionary.mixing.shiny_appearance.domain.model.LoginRequest
import ru.visionary.mixing.shiny_appearance.domain.model.RefreshRequest
import ru.visionary.mixing.shiny_appearance.domain.model.RegisterRequest

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/api/v1/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): AuthResponse

    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

}