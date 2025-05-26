package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.presentation.components.CommentItem
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.MyPostViewModel
import ru.visionary.mixing.shiny_appearance.util.downloadImageToExternalFile
import ru.visionary.mixing.shiny_appearance.util.savePictureToGallery
import ru.visionary.mixing.shiny_appearance.util.shareImage
import kotlin.math.roundToInt

@Composable
fun MyPostScreen(
    innerNavController: NavController,
    uuid: String,
    url: String,
    viewModel: MyPostViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var comment by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var writeComment by remember { mutableStateOf(false) }
    val offsetX = remember {
        Animatable(0f)
    }
    viewModel.getImageByUuid(uuid)
    val imageInfo = viewModel.imageResponse.value
    val authorNick = imageInfo?.authorNickname ?: ""

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo((offsetX.value + dragAmount).coerceAtLeast(0f))
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            if (offsetX.value > 300f) {
                                offsetX.animateTo(
                                    targetValue = 1000f,
                                    animationSpec = tween(durationMillis = 200)
                                )
                                innerNavController.navigate("profile")
                            } else {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 200)
                                )
                            }
                        }
                    }
                )
            }
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .padding(start = 8.dp, end = 8.dp, top = 5.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = "Image",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp), horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "@" + authorNick,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 35.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "Приватность",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "Поделиться",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            coroutineScope.launch {
                                val uri = downloadImageToExternalFile(context, url)
                                if (uri != null) {
                                    shareImage(context, uri)
                                } else {
                                    Toast
                                        .makeText(context, "Ошибка загрузки", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = "Сохранить",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            coroutineScope.launch {
                                val uri = downloadImageToExternalFile(context, url)
                                if (uri != null) {
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
                                } else {
                                    Toast
                                        .makeText(context, "Ошибка загрузки", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                )
            }
            val immutableList = listOf(
                "Дима: Отличное фото!",
                "Даниил: Отличное фото!",
                "Саша: Отличное фото!",
                "Максим: Отличное фото!",
                "Данила: Отличное фото!"
            )
            val list = remember { mutableStateListOf(*immutableList.toTypedArray()) }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(list) { text ->
                    CommentItem(text = text)
                }
            }

            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    if (interaction is FocusInteraction.Focus) {
                        writeComment = true
                    }
                }
            }

            Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { writeComment = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        TextField(value = comment,
                            textStyle = TextStyle(fontSize = 13.sp),
                            onValueChange = { newText ->
                                if (newText.length <= 255) {
                                    comment = newText
                                }
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.comment),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 13.sp
                                )
                            },
                            interactionSource = interactionSource,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onSurface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {

                                }
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "icon",
                            tint = if (writeComment) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Transparent
                            },
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clickable {
                                    list.add("Нелли: " + comment)
                                    comment = ""
                                    writeComment = false
                                }
                        )
                    }
                }
            }
        }
    }
}
