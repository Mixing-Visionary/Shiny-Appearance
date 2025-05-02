package ru.visionary.mixing.shiny_appearance.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.visionary.mixing.shiny_appearance.R

fun shareImage(context: Context, imageUri: Uri) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val s = R.string.share_picture
    val chooser = Intent.createChooser(shareIntent, "Отправить изображение через...")
    context.startActivity(chooser)
}
