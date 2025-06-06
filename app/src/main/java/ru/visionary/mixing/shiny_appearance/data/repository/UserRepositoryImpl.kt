package ru.visionary.mixing.shiny_appearance.data.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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

    override suspend fun deleteCurrentUser(): Result<Unit> {
        return try {
            val response = userService.deleteCurrentUser()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка обновления: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(userId: Int): Result<UserResponse> {
        return try {
            val response = userService.getOtherUser(userId)
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

    override suspend fun updateCurrentUser(
        nickname: String?,
        description: String?,
        password: String?
    ): Result<Unit> {
        return try {
            val nicknameBody = nickname?.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordBody = password?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = userService.updateCurrentUser(nicknameBody, descriptionBody, passwordBody)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка обновления: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}