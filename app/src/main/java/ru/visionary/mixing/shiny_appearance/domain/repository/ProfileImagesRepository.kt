package ru.visionary.mixing.shiny_appearance.domain.repository

import ru.visionary.mixing.shiny_appearance.domain.model.ProfileImagesResponse

interface ProfileImagesRepository {
    suspend fun getUserImages(
        size: Int,
        page: Int,
        protection: String
    ): Result<ProfileImagesResponse>

    suspend fun getOtherUserImages(
        userId: Int,
        size: Int,
        page: Int,
        protection: String
    ): Result<ProfileImagesResponse>
}