package ru.visionary.mixing.shiny_appearance.data.repository

import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.data.remote.api.AuthService
import ru.visionary.mixing.shiny_appearance.domain.model.AuthResponse
import ru.visionary.mixing.shiny_appearance.domain.model.LoginRequest
import ru.visionary.mixing.shiny_appearance.domain.model.RefreshRequest
import ru.visionary.mixing.shiny_appearance.domain.model.RegisterRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage
) {
    suspend fun login(email: String, password: String): AuthResponse? = try {
        authService.login(LoginRequest(email, password))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun refreshToken(): AuthResponse? {
        val refresh = tokenStorage.getRefreshToken() ?: return null
        return try {
            authService.refresh(RefreshRequest(refresh))
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(nickname: String, email: String, password: String): AuthResponse? = try {
        authService.register(RegisterRequest(nickname, email, password))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}