package ru.visionary.mixing.shiny_appearance.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.MainScreenViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MainScreen(
    parentNavController: NavController,
    innerNavController: NavController,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val gridState = rememberLazyGridState()
    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible to totalItems
        }.collect { (lastVisible, totalItems) ->
            if (lastVisible >= totalItems - 6) {
                viewModel.loadNextPagePublic()
            }
        }
    }
    val images by viewModel.images.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    Column(modifier = Modifier.fillMaxSize()) {

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