package ru.visionary.mixing.shiny_appearance.domain.repository

import ru.visionary.mixing.shiny_appearance.domain.model.AuthResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResponse?

    suspend fun refreshToken(): AuthResponse?

    suspend fun register(nickname: String, email: String, password: String): AuthResponse?

}