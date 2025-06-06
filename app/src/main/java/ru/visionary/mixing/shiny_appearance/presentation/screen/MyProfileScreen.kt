package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.MyProfileViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    parentNavController: NavController,
    innerNavController: NavController,
    viewModel: MyProfileViewModel = hiltViewModel()
) {

    val gridState = rememberLazyGridState()
    val isATop = gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
    val hasEnoughItemsForScroll = (gridState.layoutInfo.totalItemsCount >= 13)

    val shouldShowProfile = isATop || !hasEnoughItemsForScroll
    var selectedButton by remember { mutableStateOf(0) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val publicPosts by viewModel.publicPosts.collectAsState()
    val privatePosts by viewModel.privatePosts.collectAsState()
    val descFromVm by viewModel.description.collectAsState()
    val likesFromVm by viewModel.likes.collectAsState()
    var textDescription by remember(descFromVm) {
        mutableStateOf(descFromVm)
    }
    var likes by remember(likesFromVm) {
        mutableStateOf(likesFromVm)
    }
    val nicknameFromVm by viewModel.nickname.collectAsState()
    var textNik by remember("@" + nicknameFromVm) {
        mutableStateOf("@" + nicknameFromVm)
    }

    var isFocused by remember { mutableStateOf(false) }


    LaunchedEffect(gridState, selectedButton) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible to totalItems
        }.collect { (lastVisible, totalItems) ->
            if (lastVisible >= totalItems - 6) {
                when (selectedButton) {
                    0 -> viewModel.loadNextPagePublic()
                    1 -> viewModel.loadNextPagePrivate()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    val maxLines = 5
    val maxCharsDesc = 255
    val maxCharsNik = 25
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            focusManager.clearFocus()
        }) {
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
                            fontWeight = FontWeight.Bold,
                        ),
                        singleLine = true,
                        onValueChange = {},
                        readOnly = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.onSurface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ), modifier = Modifier
                            .wrapContentWidth()
                            .focusRequester(focusRequester)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {

                            }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.people),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "people",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    parentNavController.navigate("followingScreen")
                                }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.stats),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "stats",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    parentNavController.navigate("followersScreen")
                                }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "like",
                                modifier = Modifier
                                    .size(50.dp)
                            )
                            Text(
                                text = likes.toString(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.users_description),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(20.dp), horizontalArrangement = Arrangement.Center
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
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                            ),
                            onValueChange = { newText ->
                                val lines = newText.lines()
                                if (lines.size <= maxLines && newText.length <= maxCharsDesc) {
                                    textDescription = newText
                                    viewModel.updateUser(null, textDescription, null)
                                }
                            },
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
                                .wrapContentWidth()
                                .focusRequester(focusRequester)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {

                                }
                        )
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { selectedButton = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == 0) MaterialTheme.colorScheme.surface else Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.publications))
            }
            Button(
                onClick = { selectedButton = 1 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == 1) MaterialTheme.colorScheme.surface else Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.personal))
            }
        }
        if (selectedButton == 0 && publicPosts.isNotEmpty()) {
            listOfPosts(publicPosts, gridState, innerNavController)
        } else if (selectedButton == 1 && privatePosts.isNotEmpty()) {
            listOfPosts(privatePosts, gridState, innerNavController)
        }

    }

}

@Composable
fun listOfPosts(
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
                        innerNavController.navigate("myPost?uuid=${imageRes.uuid}&url=$encodedUrl")
                    }
            )
        }
    }
}
