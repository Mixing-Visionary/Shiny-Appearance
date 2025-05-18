package ru.visionary.mixing.shiny_appearance.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface ImageRepository {
    suspend fun saveImage(image: MultipartBody.Part, protection: RequestBody): ResponseBody
}