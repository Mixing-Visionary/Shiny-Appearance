package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    val isLoggedIn = tokenStorage.accessToken
        .map { !it.isNullOrEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(email: String, password: String): Boolean {
        var success = false
        viewModelScope.launch {
            val result = repository.login(email, password)
            if (result != null) {
                success = true
                tokenStorage.saveTokens(result.accessToken, result.refreshToken)
            }
        }
        return success
    }

    fun register(nickname: String, email: String, password: String) {
        viewModelScope.launch {
            val result = repository.register(nickname, email, password)
            if (result != null) {
                tokenStorage.saveTokens(result.accessToken, result.refreshToken)
            }
        }
    }

    fun logout() {
        viewModelScope.launch { tokenStorage.clearTokens() }
    }
}