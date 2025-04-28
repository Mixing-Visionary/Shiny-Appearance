package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import ru.visionary.mixing.shiny_appearance.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreatePicture(navController: NavController, role: String) {
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            selectedImageUri = cameraImageUri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
        }
    }

    fun requestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
        permissionLauncher.launch(permissions)
    }

    fun createImageFile(context: Context): Uri? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    LaunchedEffect(Unit) {
        requestPermissions()
    }

    // Временная вёрстка
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.clickable {
                val uri = createImageFile(context)
                cameraImageUri = uri
                uri?.let { takePictureLauncher.launch(it) }
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.take_photo),
                        modifier = Modifier.padding(top = 1.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "camera",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(180.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clickable { pickImageLauncher.launch("image/*") }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.choose_photo),
                        modifier = Modifier.padding(top = 1.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.media),
                        contentDescription = "media",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(180.dp)

                    )
                }
            }
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                )
            }// Это изображение потом будет передаваться на экран обработки изображений, пока просто для наглядности выводим
        }
    }
}