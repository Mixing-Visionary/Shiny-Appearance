package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor() : ViewModel() {
    fun validateCredentials(
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return password == confirmPassword &&
                password.length > 7 &&
                username.length in 4..25
    }
}