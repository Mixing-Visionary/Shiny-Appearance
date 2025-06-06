package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.util.saveBitmapToExternalFile
import ru.visionary.mixing.shiny_appearance.util.savePictureToGallery
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPostProcessingScreen(navController: NavController, uri: Uri) {
    val context = LocalContext.current

    val list = listOf(
        R.drawable.brightness,
        R.drawable.post_processing3
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    val sliderValues =
        remember { mutableStateListOf<Float>().apply { repeat(list.size) { add(50f) } } }
    var currentSlider by remember { mutableFloatStateOf(sliderValues[0]) }
    var processedUri by remember { mutableStateOf<Uri?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    LaunchedEffect(selectedIndex) {
        currentSlider = sliderValues[selectedIndex]
    }

    fun processImage(brightness: Float, contrast: Float) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            isProcessing = true
            val originalBitmap = uriToBitmap(context, uri)
            originalBitmap?.let {
                val updatedBitmap = applyAdjustments(it, brightness, contrast)
                processedUri = saveBitmapToExternalFile(context, updatedBitmap)
            }
            isProcessing = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = { navController.navigate("photoNeuralProcessingScreen?uri=$uri") },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp)
                )
            }

            IconButton(
                onClick = {
                    processedUri?.let {
                        navController.navigate("photoFinalProcessingScreen?uri=$it")
                    } ?: run { navController.navigate("photoFinalProcessingScreen?uri=$uri") }
                },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Вперёд",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isProcessing) {
                CircularProgressIndicator()
            } else {
                Image(
                    painter = rememberAsyncImagePainter(processedUri ?: uri),
                    contentDescription = "Edited Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.save),
            contentDescription = "Сохранить",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(50.dp)
                .padding(top = 5.dp)
                .clickable {
                    processedUri?.let {
                        savePictureToGallery(
                            context = context,
                            uri = it,
                            onSuccess = {
                                Toast
                                    .makeText(context, "Сохранено в галерею", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            onError = {
                                Toast
                                    .makeText(context, "Ошибка при сохранении", Toast.LENGTH_LONG)
                                    .show()
                            }
                        )
                    }
                }
        )

        Spacer(modifier = Modifier.weight(1f))

        Slider(
            value = currentSlider,
            onValueChange = { currentSlider = it },
            onValueChangeFinished = {
                sliderValues[selectedIndex] = currentSlider
                processImage(
                    brightness = sliderValues[0],
                    contrast = sliderValues[1]
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surface,
                thumbColor = MaterialTheme.colorScheme.primary
            ),
            thumb = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = currentSlider.roundToInt().toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .offset(y = (-15).dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .padding(horizontal = 9.dp, vertical = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .offset(y = -13.dp)
                            .size(width = 2.dp, height = 32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(list) { index, imageRes ->
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .border(
                                width = 5.dp,
                                color = if (index == selectedIndex) MaterialTheme.colorScheme.primary
                                else Color.White,
                                shape = CircleShape
                            )
                            .clip(shape = CircleShape)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .clickable {
                                selectedIndex = index
                                currentSlider = sliderValues[index]
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .padding(10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}


fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun applyAdjustments(bitmap: Bitmap, brightness: Float, contrast: Float): Bitmap {
    val adjustedBrightness = (brightness - 50f) * 2f
    val adjustedContrast = contrast / 50f

    val contrastMatrix = ColorMatrix().apply {
        setScale(adjustedContrast, adjustedContrast, adjustedContrast, 1f)
    }

    val brightnessMatrix = ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, adjustedBrightness,
            0f, 1f, 0f, 0f, adjustedBrightness,
            0f, 0f, 1f, 0f, adjustedBrightness,
            0f, 0f, 0f, 1f, 0f
        )
    )

    contrastMatrix.postConcat(brightnessMatrix)

    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(contrastMatrix)
    }

    val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config!!)
    Canvas(result).drawBitmap(bitmap, 0f, 0f, paint)
    return result
}


