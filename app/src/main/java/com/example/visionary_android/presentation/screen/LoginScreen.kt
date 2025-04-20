package com.example.visionary_android.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.visionary_android.R

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background),
                contentScale = ContentScale.Crop
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var login = remember {
            mutableStateOf("")
        }
        Text(
            textAlign = TextAlign.End,
            fontSize = 13.sp,  // Размер текста в sp
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                    append("Пропустить")
                }
            },
            modifier = Modifier
                .padding(top = 25.dp, bottom = 25.dp, end = 10.dp)
                .fillMaxWidth()
        )
        Image(
            painter = painterResource(id = R.drawable.label),
            modifier = Modifier
                .width(200.dp)
                .height(80.dp),
            contentDescription = null
        )

        Button(
            onClick = { /* Обработка нажатия */ },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onTertiary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 360.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                )
                Text(
                    text = "Войти с помощью Google",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Button(
            onClick = { navController.navigate("registrationScreen") },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onTertiary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 30.dp, end = 30.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 7.dp, bottom = 7.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MailOutline,
                    contentDescription = "email",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = "Войти в аккаунт с эл. почтой",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            val annotatedText = buildAnnotatedString {
                // Не кликабельная часть 1
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                    append("Регистрируясь, ты соглашаешься с \n")
                }

                // Кликабельная часть 2 (Условия использования)
                pushStringAnnotation(tag = "TERMS", annotation = "terms_click")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                ) {
                    append("Условиями использования ")
                }
                pop()

                // Не кликабельная часть 3
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                    append("и\n")
                }

                // Кликабельная часть 4 (Политика конфиденциальности)
                pushStringAnnotation(tag = "PRIVACY", annotation = "privacy_click")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                ) {
                    append("Политикой конфиденциальности")
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
                                    // Обработка клика по условиям использования
                                    println("Открыть условия использования")
                                }

                                "PRIVACY" -> {
                                    // Обработка клика по политике конфиденциальности
                                    println("Открыть политику конфиденциальности")
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
                fontSize = 13.sp,  // Размер текста в sp
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                        append("Ещё нет аккаунта?")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                        append(" Зарегестрироваться ")
                    }
                },
                modifier = Modifier.padding(bottom = 50.dp)
            )
        }
    }

}