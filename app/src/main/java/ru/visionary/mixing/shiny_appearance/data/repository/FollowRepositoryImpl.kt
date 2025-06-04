package ru.visionary.mixing.shiny_appearance.data.repository

import ru.visionary.mixing.shiny_appearance.data.remote.api.FollowService
import ru.visionary.mixing.shiny_appearance.domain.model.UsersResponse
import ru.visionary.mixing.shiny_appearance.domain.repository.FollowRepository
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val followService: FollowService
) : FollowRepository {

    override suspend fun followUser(userId: Int): Result<Unit> {
        return try {
            val response = followService.followUser(userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun followUserDelete(userId: Int): Result<Unit> {
        return try {
            val response = followService.followUserDelete(userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFollows(size: Int, page: Int): Result<UsersResponse> {
        return try {
            val response = followService.getFollows(size, page)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Пустое тело ответа"))
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFollowsById(userId: Int, size: Int, page: Int): Result<UsersResponse> {
        return try {
            val response = followService.getFollowsById(userId, size, page)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Пустое тело ответа"))
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFollowers(size: Int, page: Int): Result<UsersResponse> {
        return try {
            val response = followService.getFollowers(size, page)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Пустое тело ответа"))
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
