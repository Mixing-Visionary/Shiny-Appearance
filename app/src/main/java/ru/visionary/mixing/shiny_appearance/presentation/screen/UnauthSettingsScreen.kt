package ru.visionary.mixing.shiny_appearance.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.presentation.components.SettingsItem

@Composable
fun UnauthSettingsScreen(navController: NavController) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.settings),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier
        .padding(top = 30.dp)
        .fillMaxSize()) {

        Text(
            text = stringResource(id = R.string.account),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp, top = 10.dp)
        )
        SettingsItem(
            text = stringResource(id = R.string.enter_to_account),
            icon = R.drawable.logout,
            onClick = {
                navController.navigate("authorizationScreen")
            })
        Text(
            text = stringResource(id = R.string.personalization),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp, top = 10.dp)
        )
        SettingsItem(text = stringResource(id = R.string.change_theme), icon = null, onClick = {})

    }

}