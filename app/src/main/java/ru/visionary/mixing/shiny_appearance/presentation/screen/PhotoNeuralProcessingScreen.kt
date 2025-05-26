package ru.visionary.mixing.shiny_appearance.presentation.screen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.ProcessingViewModel
import ru.visionary.mixing.shiny_appearance.util.savePictureToGallery
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoNeuralProcessingScreen(
    navController: NavController,
    uri: Uri,
    viewModel: ProcessingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val list = listOf(
        R.drawable.without_style,
        R.drawable.style1,
        R.drawable.style2,
        R.drawable.style3,
        R.drawable.style4,
        R.drawable.style5,
    )
    val styleNames = listOf(
        "none", "anime", "ghibli", "cyberpunk", "noir", "gogh"
    )
    val chooseStyle = stringResource(id = R.string.choose_style)
    var textStyle by remember {
        mutableStateOf(chooseStyle)
    }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val sliderValues =
        remember { mutableStateListOf<Float>().apply { repeat(list.size) { add(50f) } } }

    val sliderPosition = sliderValues[selectedIndex]
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
                onClick = { navController.navigate("mainTabsScreen?index=1") },
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
                    if (selectedIndex == 0) {
                        navController.navigate("photoPostProcessingScreen?uri=$uri")
                    } else {
                        val style = styleNames[selectedIndex]
                        val strength = sliderValues[selectedIndex] / 100f
                        viewModel.processImage(
                            uri = uri,
                            style = style,
                            strength = strength,
                            onSuccess = { generatedUuid ->
                                navController.navigate("waitingScreen?uuid=$generatedUuid")
                            },
                            onError = {
                                Toast.makeText(
                                    context,
                                    "Ошибка обработки изображения",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
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
                .padding(start = 8.dp, end = 8.dp, top = 5.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.save),
            contentDescription = "Сохранить",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(50.dp)
                .padding(top = 5.dp)
                .clickable(interactionSource = remember { MutableInteractionSource() },
                    indication = null) {
                    savePictureToGallery(
                        context = context,
                        uri = uri,
                        onSuccess = {
                            Toast
                                .makeText(
                                    context,
                                    "Сохранено в галерею",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                        onError = { e ->
                            Toast
                                .makeText(
                                    context,
                                    "Ошибка при сохранении",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    )
                }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = textStyle,
            style = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 25.dp)
        )
        if (selectedIndex != 0) {
            Slider(
                value = sliderPosition,
                onValueChange = { sliderValues[selectedIndex] = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surface,
                    thumbColor = MaterialTheme.colorScheme.primary
                ),
                thumb = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = sliderPosition.roundToInt().toString(),
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
                },
                valueRange = 0f..100f
            )
        }
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
                                if (selectedIndex == 0) {
                                    textStyle = chooseStyle
                                } else {
                                    textStyle = "Выбран стиль: " + styleNames[selectedIndex]
                                }
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