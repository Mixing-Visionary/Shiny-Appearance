package ru.visionary.mixing.shiny_appearance.data.repository

import ru.visionary.mixing.shiny_appearance.data.remote.api.ProfileImagesService
import ru.visionary.mixing.shiny_appearance.domain.model.ProfileImagesResponse
import ru.visionary.mixing.shiny_appearance.domain.repository.ProfileImagesRepository
import javax.inject.Inject

class ProfileImagesRepositoryImpl @Inject constructor(
    private val service: ProfileImagesService
) : ProfileImagesRepository {
    override suspend fun getUserImages(
        size: Int,
        page: Int,
        protection: String
    ): Result<ProfileImagesResponse> {
        return try {
            val response = service.getUserImages(size, page, protection)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}