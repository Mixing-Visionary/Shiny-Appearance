package ru.visionary.mixing.shiny_appearance.domain.repository

import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse

interface UserRepository {
    suspend fun getCurrentUser(): Result<UserResponse>
}