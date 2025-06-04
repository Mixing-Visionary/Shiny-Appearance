package ru.visionary.mixing.shiny_appearance.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.MainScreenViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    parentNavController: NavController,
    innerNavController: NavController,
    viewModel: MainScreenViewModel = hiltViewModel()
    ) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("new", "popular", "follow")
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    val gridState = rememberLazyGridState()
    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible to totalItems
        }.collect { (lastVisible, totalItems) ->
            if (lastVisible >= totalItems - 6) {
                viewModel.loadNextPagePublic(selectedOptionText)
            }
        }
    }
    val images by viewModel.images.collectAsState()
    LaunchedEffect(selectedOptionText) {
        gridState.scrollToItem(0)
        viewModel.refresh(selectedOptionText)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOptionText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painter = if (expanded) painterResource(id = R.drawable.arrow_drop_up) else painterResource(id = R.drawable.arrow_drop_down),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                    focusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.onTertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.onTertiary
                ),
                modifier = Modifier
                    .menuAnchor()
                    .width(180.dp).padding(horizontal = 12.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(18.dp),
                        clip = false
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(18.dp)
                    )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Feed(list = images, gridState = gridState, innerNavController = innerNavController)
    }
}

@Composable
fun Feed(
    list: List<DisplayImage>,
    gridState: LazyGridState, innerNavController: NavController
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