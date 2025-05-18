package ru.visionary.mixing.shiny_appearance.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.visionary.mixing.shiny_appearance.data.remote.api.ImageService
import ru.visionary.mixing.shiny_appearance.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(private val imageService: ImageService):ImageRepository {
    override suspend fun saveImage(image: MultipartBody.Part, protection: RequestBody) =
        imageService.saveImage(image, protection)
}