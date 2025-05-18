package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.visionary.mixing.shiny_appearance.data.repository.ImageRepositoryImpl
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageSaveViewModel @Inject constructor(
    private val imageRepository: ImageRepositoryImpl
) : ViewModel() {

    fun saveImage(context: Context, imageUri: Uri, protection: String) {
        viewModelScope.launch {
            try {
                val mimeType = getMimeType(context, imageUri) ?: "image/jpeg"
                Log.d("ImageSaveViewModel", "MIME Type: $mimeType")

                val imageFile = uriToFile(context, imageUri, mimeType) ?: run {
                    Log.e("ImageSaveViewModel", "Не удалось преобразовать Uri в файл")
                    return@launch
                }

                val imageRequestBody = imageFile.asRequestBody(mimeType.toMediaTypeOrNull())

                val imagePart = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = imageFile.name,
                    body = imageRequestBody
                )

                val protectionRequestBody = protection.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = imageRepository.saveImage(imagePart, protectionRequestBody)

                Log.d("ImageSaveViewModel", "Изображение успешно сохранено: $response")

            } catch (e: Exception) {
                Log.e("ImageSaveViewModel", "Ошибка при сохранении изображения: ${e.message}", e)
            }
        }
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }

    private fun uriToFile(context: Context, uri: Uri, mimeType: String): File? {
        return try {
            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(mimeType)
                ?: "jpg"

            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("image", ".$extension", context.cacheDir)
            tempFile.deleteOnExit()

            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

