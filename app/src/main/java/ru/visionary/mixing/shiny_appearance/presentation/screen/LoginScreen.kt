package ru.visionary.mixing.shiny_appearance.presentation.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.visionary.mixing.shiny_appearance.R

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
            fontSize = 13.sp,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiary)) {
                    append(stringResource(R.string.skip))
                }
            },
            modifier = Modifier
                .padding(top = 25.dp, bottom = 25.dp, end = 10.dp)
                .fillMaxWidth()
        )
        Image(
            painter = painterResource(id = R.drawable.visionary_label),
            modifier = Modifier
                .width(200.dp)
                .height(80.dp),
            contentDescription = "kk"
        )

        Button(
            onClick = {  },
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
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = (stringResource(R.string.google_entry)),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Button(
            onClick = { navController.navigate("authorizationScreen") },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onTertiary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, start = 30.dp, end = 30.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.MailOutline,
                    contentDescription = "email",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = (stringResource(R.string.email_entry)),
                    fontSize = 15.sp,
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
                modifier = Modifier.padding(bottom = 50.dp)
            )
        }
    }

}