package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.data.repository.AuthRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
    private val tokenStorage: TokenStorage,
) : ViewModel() {
    val isLoggedIn = tokenStorage.accessToken
        .map { !it.isNullOrEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(
        email: String,
        password: String,
        callback: (success: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                if (result != null) {
                    tokenStorage.saveTokens(result.accessToken, result.refreshToken)
                    callback(true)
                } else {
                    callback(false)
                }
            } catch (e: Exception) {
                callback(false)
            }
        }
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