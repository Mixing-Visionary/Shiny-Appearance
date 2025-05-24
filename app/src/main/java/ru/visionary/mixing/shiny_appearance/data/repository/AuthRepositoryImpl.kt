package ru.visionary.mixing.shiny_appearance.data.repository

import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.data.remote.api.AuthService
import ru.visionary.mixing.shiny_appearance.domain.model.AuthResponse
import ru.visionary.mixing.shiny_appearance.domain.model.LoginRequest
import ru.visionary.mixing.shiny_appearance.domain.model.RefreshRequest
import ru.visionary.mixing.shiny_appearance.domain.model.RegisterRequest
import ru.visionary.mixing.shiny_appearance.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): AuthResponse? = try {
        authService.login(LoginRequest(email, password))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override suspend fun refreshToken(): AuthResponse? {
        val refresh = tokenStorage.getRefreshToken() ?: return null
        return try {
            authService.refresh(RefreshRequest(refresh))
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun register(nickname: String, email: String, password: String): AuthResponse? = try {
        authService.register(RegisterRequest(nickname, email, password))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}