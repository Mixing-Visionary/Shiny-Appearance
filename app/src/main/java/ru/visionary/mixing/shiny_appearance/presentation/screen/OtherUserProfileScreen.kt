package ru.visionary.mixing.shiny_appearance.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.MyProfileViewModel
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.OtherProfileViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherUserProfileScreen(
    innerNavController: NavController, userId: Int,
    viewModel: OtherProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.refresh(userId)
    }
    val gridState = rememberLazyGridState()
    val isATop = gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
    val hasEnoughItemsForScroll = (gridState.layoutInfo.totalItemsCount >= 13)

    val shouldShowProfile = isATop || !hasEnoughItemsForScroll
    val publicPosts by viewModel.publicPosts.collectAsState()
    val descFromVm by viewModel.description.collectAsState()
    var textDescription by remember(descFromVm) {
        mutableStateOf(descFromVm)
    }
    val nicknameFromVm by viewModel.nickname.collectAsState()
    var textNik by remember("@" + nicknameFromVm) {
        mutableStateOf("@" + nicknameFromVm)
    }

    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible to totalItems
        }.collect { (lastVisible, totalItems) ->
            if (lastVisible >= totalItems - 6) {
                viewModel.loadNextPagePublic(userId)
            }
        }
    }

    val offsetX = remember {
        Animatable(0f)
    }
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
                                innerNavController.navigate("mainScreen")
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
        ) {
            AnimatedVisibility(
                visible = shouldShowProfile,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Image(
                        painter = painterResource(id = R.drawable.avatar), // Потом будет ава юзера
                        contentDescription = "Show password",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(150.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        TextField(
                            value = textNik,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            ),
                            onValueChange = {},
                            enabled = false,
                            colors = TextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onSurface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ), modifier = Modifier
                                .wrapContentWidth()
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                            .padding(top = 3.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.people),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "people",
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.person_add),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "stats",
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "like",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.users_description),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .wrapContentWidth()
                        ) {
                            TextField(
                                value = textDescription,
                                textStyle = TextStyle(fontSize = 13.sp),
                                onValueChange = {},
                                enabled = false,
                                colors = TextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    cursorColor = MaterialTheme.colorScheme.onSurface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                ),
                                modifier = Modifier
                                    .wrapContentWidth()
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = stringResource(id = R.string.publications))
                }
            }
            listOfOtherUserPosts(publicPosts, gridState, innerNavController)
        }
    }
}

@Composable
fun listOfOtherUserPosts(
    list: List<DisplayImage>,
    gridState: LazyGridState,
    innerNavController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = gridState
    ) {
        items(list) { imageRes ->
            Image(
                painter = rememberAsyncImagePainter(imageRes.url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(170.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        val encodedUrl =
                            URLEncoder.encode(imageRes.url, StandardCharsets.UTF_8.toString())
                        innerNavController.navigate("otherPost?uuid=${imageRes.uuid}&url=$encodedUrl")
                    }
            )
        }
    }
}