package ru.visionary.mixing.shiny_appearance.data.repository

import ru.visionary.mixing.shiny_appearance.data.remote.api.UserService
import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse
import ru.visionary.mixing.shiny_appearance.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override suspend fun getCurrentUser(): Result<UserResponse> {
        return try {
            val response = userService.getCurrentUser()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Пустое тело ответа"))
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}