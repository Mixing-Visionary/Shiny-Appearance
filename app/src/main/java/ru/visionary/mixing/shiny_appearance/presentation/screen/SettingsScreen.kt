package ru.visionary.mixing.shiny_appearance.presentation.screen

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.visionary.mixing.shiny_appearance.R
import ru.visionary.mixing.shiny_appearance.presentation.components.SettingsItem
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.AuthViewModel
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.MyProfileViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    myProfileViewModel: MyProfileViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var newpassword by remember { mutableStateOf("") }
    var isVisibleChangePass by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }

    var proDialog by remember { mutableStateOf(false) }
    var premiumDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPrefs =
        remember { context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE) }

    var notifEnabled by remember {
        mutableStateOf(sharedPrefs.getBoolean("notif", true))
    }
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
        .fillMaxSize()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            focusManager.clearFocus()
        }) {
        Text(
            text = stringResource(id = R.string.subscription),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp, top = 20.dp)
        )

        SettingsItem(
            text = stringResource(id = R.string.pro), icon = null,
            onClick = { proDialog = true })
        SettingsItem(
            text = stringResource(id = R.string.premium), icon = null,
            onClick = { premiumDialog = true })

        Text(
            text = stringResource(id = R.string.notify),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp, top = 10.dp)
        )
        SettingsItem(
            text = stringResource(
                id = R.string.notify
            ), icon = if (notifEnabled) {
                R.drawable.bell
            } else {
                R.drawable.notif_off
            }, onClick = {
                val newValue = !notifEnabled
                sharedPrefs.edit().putBoolean("notif", newValue).apply()
                notifEnabled = newValue
            })
        Text(
            text = stringResource(id = R.string.account),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp, top = 10.dp)
        )
        SettingsItem(
            text = stringResource(id = R.string.change_password), icon = null,
            onClick = { isVisibleChangePass = true })
        if (isVisibleChangePass) {
            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp)) {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = false
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(value = newpassword,
                            textStyle = TextStyle(fontSize = 13.sp),
                            onValueChange = { newText -> newpassword = newText },
                            placeholder = {
                                if (!showError) {
                                    Text(
                                        text = stringResource(id = R.string.enter_new_password),
                                        fontSize = 13.sp
                                    )
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.error_new_password),
                                        color = Color.Red,
                                        fontSize = 13.sp
                                    )
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

                                })

                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "check",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.clickable {
                                if (newpassword.length > 7) {
                                    isVisibleChangePass = false
                                    myProfileViewModel.updateUser(null, null, newpassword)
                                } else {
                                    newpassword = ""
                                    showError = true
                                }
                            }
                        )
                    }
                }
            }
        }
        SettingsItem(
            text = stringResource(id = R.string.logout),
            icon = R.drawable.logout,
            onClick = {
                authViewModel.logout()
                navController.navigate("loginScreen") {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            })
        SettingsItem(
            text = stringResource(id = R.string.delete_account),
            icon = R.drawable.delete,
            onClick = {
                isDeleting = true
            })

        if (isDeleting) {
            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp)) {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = false
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(value = newpassword,
                            textStyle = TextStyle(fontSize = 13.sp),
                            readOnly = true,
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.is_deleting_account),
                                    color = Color.Red,
                                    fontSize = 13.sp
                                )
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

                                })
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.yes),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.check),
                                contentDescription = "delete",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.clickable {
                                    authViewModel.logout()
                                    myProfileViewModel.deleteCurrentUser()
                                    navController.navigate("loginScreen") {
                                        popUpTo(0) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = stringResource(id = R.string.personalization),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp, top = 10.dp)
        )
        SettingsItem(text = stringResource(id = R.string.change_theme), icon = null, onClick = {})

    }
    if (proDialog) {
        CustomProDialog(onDismiss = { proDialog = false })
    }
    if (premiumDialog) {
        CustomPremiumDialog(onDismiss = { premiumDialog = false })
    }

}

@Composable
fun CustomProDialog(onDismiss: () -> Unit) {
    var showLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                if (!showLoading) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.pro_part1),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 30.dp)
                            )
                        }
                        Text(
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(stringResource(R.string.pro_part2))
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append(stringResource(R.string.pro_part3))
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(stringResource(R.string.pro_part4))
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append(stringResource(R.string.pro_part5))
                                }
                            }
                        )

                        Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 30.dp)) {
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(48.dp),
                                        clip = false
                                    )
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .clickable { showLoading=true }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "Оплатить на месяц",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }

                        Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 30.dp)) {
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(48.dp),
                                        clip = false
                                    )
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .clickable { showLoading=true }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "Оплатить на год",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                }else{
                    if (!showSuccess) {
                        LaunchedEffect(Unit) {
                            delay(2500)
                            showSuccess = true
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .background(Color.Transparent)
                                    .padding(top = 30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(100.dp),
                                    strokeWidth = 6.dp
                                )
                            }
                            Text(
                                text = "В процессе оплаты",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 50.dp, bottom = 30.dp)
                            )
                        }
                    } else {
                        LaunchedEffect(Unit) {
                            delay(500)
                            onDismiss()
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 30.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                            Text(
                                text = "Подписка оформлена",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 50.dp, bottom = 30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomPremiumDialog(onDismiss: () -> Unit) {
    var showLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                if (!showLoading) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.premium_part1),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 30.dp)
                            )
                        }
                        Text(
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(stringResource(R.string.premium_part2))
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append(stringResource(R.string.premium_part3))
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(stringResource(R.string.premium_part4))
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append(stringResource(R.string.premium_part5))
                                }
                            }
                        )

                        Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 30.dp)) {
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(48.dp),
                                        clip = false
                                    )
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .clickable { showLoading = true }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "Оплатить на месяц",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }

                        Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 30.dp)) {
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(48.dp),
                                        clip = false
                                    )
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .clickable { showLoading = true }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "Оплатить на год",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    if (!showSuccess) {
                        LaunchedEffect(Unit) {
                            delay(2500)
                            showSuccess = true
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .background(Color.Transparent)
                                    .padding(top = 30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(100.dp),
                                    strokeWidth = 6.dp
                                )
                            }
                            Text(
                                text = "В процессе оплаты",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 50.dp, bottom = 30.dp)
                            )
                        }
                    } else {
                        LaunchedEffect(Unit) {
                            delay(500)
                            onDismiss()
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 30.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                            Text(
                                text = "Подписка оформлена",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 50.dp, bottom = 30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
