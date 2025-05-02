package ru.visionary.mixing.shiny_appearance.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.visionary.mixing.shiny_appearance.R

@Composable
fun MainTabScreen(navController: NavController, index: Int, role: String) {
    val tabs = listOf(
        R.drawable.home,
        R.drawable.add,
        R.drawable.person,
        R.drawable.settings,
    )
    val tabDescriptions = listOf("Лента", "Создание картинки", "Профиль", "Настройки")
    var selectedTabIndex by remember { mutableStateOf(index) }

    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.visionary_label),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(55.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "bell",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 23.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTabIndex) {
                    0 -> MainScreen(navController, role)
                    1 -> CreatePicture(navController, role)
                    2 -> MyProfileScreen(navController, role)
                    3 -> SettingsScreen(navController, role)
                }
            }



            TabRow(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                tabs.forEachIndexed { index, icon ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = "icon",
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }

}