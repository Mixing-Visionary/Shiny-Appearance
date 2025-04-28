package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Screen(navController: NavController) {
    // Состояние для хранения URI изображения
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
// Контракт для запуска камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Фото успешно сохранено по imageUri
            // Здесь не нужно ничего делать, так как imageUri уже установлен
            // и изображение будет показано автоматически
        }
    }
    // Запрос разрешений
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: false
        val writePermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true // На Android 10+ используем MediaStore API
        } else {
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
        }

        if (cameraPermissionGranted && writePermissionGranted) {
            // Создаем URI для сохранения изображения
            val uri = createImageUri(context)
            imageUri = uri

            // Запускаем камеру с этим URI
            uri?.let {
                cameraLauncher.launch(it)
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Мой экран с камерой",
            modifier = Modifier.padding(16.dp)
        )

        // Отображаем фото, если оно есть
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Сделанное фото",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                checkAndRequestPermissions(context, permissionLauncher)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Открыть камеру")
        }

        // Другие элементы вашего Screen можно добавить здесь
    }
}

// Функция для проверки и запроса разрешений
private fun checkAndRequestPermissions(
    context: Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Array<String>>
) {
    val permissions = mutableListOf(Manifest.permission.CAMERA)

    // На Android < 10 нужно разрешение на запись в хранилище
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    val allPermissionsGranted = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    if (allPermissionsGranted) {
        // Разрешения уже есть, запускаем камеру напрямую (через launcher)
    } else {
        launcher.launch(permissions.toTypedArray())
    }
}

// Функция для создания URI изображения
private fun createImageUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_$timeStamp.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        // На Android 10+ указываем директорию
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CameraPhotoApp")
        }
    }

    return context.contentResolver.insert(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        },
        contentValues
    )
}