package ru.visionary.mixing.shiny_appearance.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.visionary.mixing.shiny_appearance.R

@Composable
fun AuthorizationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.registrationscreen),
                contentScale = ContentScale.Crop
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = { navController.navigate("loginScreen") },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Лайк",
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.size(50.dp)

                )
            }
            IconButton(
                onClick = {  },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Лайк",
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.visionary_label),
            modifier = Modifier
                .width(200.dp)
                .height(80.dp),
            contentDescription = null
        )
        var email by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = (stringResource(R.string.email_hint)),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.MailOutline,
                        contentDescription = "email",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                    focusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.onTertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )


            var password by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        text = (stringResource(R.string.password)),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "email",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.eye else R.drawable.eye
                            ),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                    focusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.onTertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 8.dp, bottom = 75.dp)
            )


            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                    append(stringResource(R.string.privacy_part1))
                }

                pushStringAnnotation(tag = "TERMS", annotation = "terms_click")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                ) {
                    append(stringResource(R.string.privacy_part2))
                }
                pop()

                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                    append(stringResource(R.string.privacy_part3))
                }

                pushStringAnnotation(tag = "PRIVACY", annotation = "privacy_click")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                ) {
                    append(stringResource(R.string.privacy_part4))
                }
                pop()
            }
            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(offset, offset)
                        .firstOrNull()?.let { annotation ->
                            when (annotation.tag) {
                                "TERMS" -> {

                                }

                                "PRIVACY" -> {

                                }
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp
                )
            )

            Text(

                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                        append(stringResource(R.string.register_part1))
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                        append(stringResource(R.string.register_part2))
                    }
                },
                modifier = Modifier.padding(bottom = 50.dp).clickable {
                    navController.navigate("registrationScreen")
                }            )
        }
    }
}