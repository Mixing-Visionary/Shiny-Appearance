package ru.visionary.mixing.shiny_appearance.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

suspend fun downloadImageToExternalFile(context: Context, url: String): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) return@withContext null

            val inputStream = connection.inputStream

            val imagesDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Visionary"
            )
            if (!imagesDir.exists()) imagesDir.mkdirs()

            val imageFile = File(imagesDir, "shared_image_${System.currentTimeMillis()}.jpg")
            imageFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
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
