package ru.visionary.mixing.shiny_appearance.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import ru.visionary.mixing.shiny_appearance.domain.model.ImageResponse
import ru.visionary.mixing.shiny_appearance.domain.model.ProtectionType
import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse

interface ImageRepository {
    suspend fun saveImage(image: MultipartBody.Part, protection: RequestBody): ResponseBody

    suspend fun getImageByUuid(uuid: String): Result<ImageResponse>

    suspend fun deleteImageByUuid(uuid: String): Result<Unit>

    suspend fun updateImagePrivacy(uuid: String, protection: ProtectionType): Result<Unit>

    suspend fun likeImage(uuid: String): Result<Unit>

    suspend fun dislikeImage(uuid: String): Result<Unit>

}