package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.presentation.components.PublishItem
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.ImageSaveViewModel
import ru.visionary.mixing.shiny_appearance.util.savePictureToGallery
import ru.visionary.mixing.shiny_appearance.util.shareImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoFinalProcessingScreen(navController: NavController, uri: Uri) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val imageSaveViewModel: ImageSaveViewModel = hiltViewModel()


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
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(
                onClick = { navController.navigate("photoPostProcessingScreen?uri=$uri") },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
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
        Row(
            modifier = Modifier
                .padding(top = 75.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.save),
                contentDescription = "Сохранить",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(50.dp)
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
            Icon(
                painter = painterResource(id = R.drawable.publish),
                contentDescription = "Опубликовать",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(50.dp)
                    .clickable(interactionSource = remember { MutableInteractionSource() },
                        indication = null) { showBottomSheet = true }
            )
            Icon(
                painter = painterResource(id = R.drawable.share),
                contentDescription = "Поделиться",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(50.dp)
                    .clickable(interactionSource = remember { MutableInteractionSource() },
                        indication = null) {
                        shareImage(context, uri)
                    }
            )
        }
        Box(
        ) {

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp), verticalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        PublishItem(
                            text = stringResource(id = R.string.to_publish),
                            onClick = {
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                imageSaveViewModel.saveImage(context, uri, "public")
                                navController.navigate("mainTabsScreen?index=2")
                            })
                        PublishItem(
                            text = stringResource(id = R.string.to_personal),
                            onClick = {
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                imageSaveViewModel.saveImage(context, uri, "private")
                                navController.navigate("mainTabsScreen?index=2")
                            })
                    }
                }
            }
        }
    }
}