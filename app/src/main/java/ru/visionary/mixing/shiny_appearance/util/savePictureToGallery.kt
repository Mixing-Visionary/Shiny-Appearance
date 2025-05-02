package ru.visionary.mixing.shiny_appearance.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.IOException

fun savePictureToGallery(
    context: Context,
    uri: Uri,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val resolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                "processed_image_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/Visionary"
            )
        }

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (imageUri != null) {
            resolver.openOutputStream(imageUri).use { outputStream ->
                resolver.openInputStream(uri).use { inputStream ->
                    if (inputStream != null && outputStream != null) {
                        inputStream.copyTo(outputStream)
                        onSuccess()
                    } else {
                        throw IOException("Input or Output stream is null")
                    }
                }
            }
        } else {
            throw IOException("Failed to create new MediaStore record.")
        }
    } catch (e: Exception) {
        onError(e)
    }
}
