package ru.visionary.mixing.shiny_appearance.domain.repository

import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse

interface UserRepository {
    suspend fun getCurrentUser(): Result<UserResponse>
    suspend fun deleteCurrentUser(): Result<Unit>
    suspend fun getUser(userId: Int): Result<UserResponse>
    suspend fun updateCurrentUser(
        nickname: String?,
        description: String?,
        password: String?
    ): Result<Unit>

}