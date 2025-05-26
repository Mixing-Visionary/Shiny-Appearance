package ru.visionary.mixing.shiny_appearance.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun saveBitmapToExternalFile(context: Context, bitmap: Bitmap): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val imagesDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Visionary"
            )
            if (!imagesDir.exists()) imagesDir.mkdirs()

            val imageFile = File(imagesDir, "processed_image_${System.currentTimeMillis()}.jpg")

            imageFile.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                imageFile
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
