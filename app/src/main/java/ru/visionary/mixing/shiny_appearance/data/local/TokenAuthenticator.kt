package ru.visionary.mixing.shiny_appearance.data.local

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.visionary.mixing.shiny_appearance.data.remote.api.AuthService
import ru.visionary.mixing.shiny_appearance.domain.model.RefreshRequest
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val refreshService: AuthService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { tokenStorage.getRefreshToken() } ?: return null

        val newTokens = try {
            runBlocking {
                refreshService.refresh(RefreshRequest(refreshToken))
            }
        } catch (e: Exception) {
            return null
        }

        if (newTokens != null) {
            runBlocking {
                tokenStorage.saveTokens(newTokens.accessToken, newTokens.refreshToken)
            }
            return response.request.newBuilder()
                .header("Authorization", "Bearer ${newTokens.accessToken}")
                .build()
        }
        return null
    }
}