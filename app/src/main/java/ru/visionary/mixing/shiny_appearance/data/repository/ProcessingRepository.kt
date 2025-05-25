package ru.visionary.mixing.shiny_appearance.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.visionary.mixing.shiny_appearance.data.remote.api.ProcessingService
import javax.inject.Inject

class ProcessingRepository @Inject constructor(
    private val api: ProcessingService
) {
    suspend fun processImage(
        context: Context,
        uri: Uri,
        style: String,
        strength: Float?
    ): String? {
        return try {
            val imagePart = createImagePartFromUri(context, uri)
            val response = api.processImage(imagePart, style, strength)
            if (response.isSuccessful) {
                response.body()?.uuid
            } else {
                Log.e("API_ERROR", "Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("NETWORK_ERROR", "Exception: ${e.message}", e)
            null
        }
    }

    private fun createImagePartFromUri(context: Context, uri: Uri): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream from URI")

        val bytes = inputStream.readBytes()
        val fileName = "image.jpg"
        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", fileName, requestBody)
    }
}
