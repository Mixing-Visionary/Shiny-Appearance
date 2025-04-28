package ru.visionary.mixing.shiny_appearance.presentation.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.navigation.NavController
import ru.visionary.mixing.shiny_appearance.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(navController: NavController, role: String) {

    val gridState = rememberLazyGridState()
    val isATop = gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
    val hasEnoughItemsForScroll = (gridState.layoutInfo.totalItemsCount >= 13)

    val shouldShowProfile = isATop || !hasEnoughItemsForScroll
    var selectedButton by remember { mutableStateOf(0) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var textDescription by remember {
        mutableStateOf(
            "Любитель поиграть в игры и посмотреть " +
                    "аниме \nНик в Telegram - @maxmattakushi"
        )
    }// временная заглушка
    var textNik by remember { mutableStateOf("@JustiSablea") } // временная заглушка


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
                            fontWeight = FontWeight.Bold
                        ),
                        onValueChange = { newText -> textNik = newText },
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
                        painter = painterResource(id = R.drawable.stats),
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
                            onValueChange = { newText -> textDescription = newText },
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

        var listPublications = listOf(
            //временные заглушки
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen, R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,

            )
        var listPersonalPosts = listOf(
            //временные заглушки
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,
            R.drawable.registrationscreen,

            )
        if (selectedButton == 0) {
            listOfPosts(listPublications, gridState)
        } else {
            listOfPosts(listPersonalPosts, gridState)
        }
    }
}

@Composable
fun listOfPosts(list: List<Int>, gridState: LazyGridState) {
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
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(170.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
    }
}
