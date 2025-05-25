package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import ru.visionary.mixing.shiny_appearance.presentation.components.PublishItem
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.WaitingViewModel
import java.io.File

@Composable
fun WaitingScreen(
    navController: NavController,
    uuid: String,
    viewModel: WaitingViewModel = hiltViewModel()
) {
    val status by viewModel.processingStatus
    val bitmap by viewModel.imageBitmap
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uuid) {
        viewModel.connectToWebSocket(uuid)
    }

    BackHandler(enabled = true) {
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(text = status,fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 40.dp))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Transparent)
                .padding(horizontal = 70.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(250.dp),
                strokeWidth = 10.dp
            )
        }
        Row(modifier = Modifier.padding(start = 40.dp, end = 40.dp, top = 40.dp)) {
            PublishItem(text = "Отменить обработку", onClick = {
                viewModel.cancelProcessing()
                val index = 1
                navController.navigate("mainTabsScreen?index=$index") {
                    popUpTo("waitingScreen") { inclusive = true }
                }
            }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        bitmap?.let { bmp ->
            isLoading = false
            val context = LocalContext.current
            val uri = bitmapToUri(context, bmp).toString()
            navController.navigate("photoPostProcessingScreen?uri=$uri") {
                popUpTo("waitingScreen") { inclusive = true }
            }
        }

    }
}

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "processed_image_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return Uri.fromFile(file)
}


