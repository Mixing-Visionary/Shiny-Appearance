package ru.visionary.mixing.shiny_appearance.presentation.viewmodel


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.*
import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.domain.model.ProcessingMessage
import javax.inject.Inject


@HiltViewModel
class WaitingViewModel @Inject constructor(
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _processingStatus = mutableStateOf("Обработка...")
    val processingStatus: State<String> = _processingStatus

    private val _imageBitmap = mutableStateOf<Bitmap?>(null)
    val imageBitmap: State<Bitmap?> = _imageBitmap

    private var webSocket: WebSocket? = null

    fun connectToWebSocket(uuid: String) {
        viewModelScope.launch {
            val token = tokenStorage.getAccessToken()

            if (token == null) {
                _processingStatus.value = "Нет токена для подключения"
                return@launch
            }

            val request = Request.Builder()
                .url("ws://185.188.182.20:8080/ws/processing?uuid=$uuid")
                .addHeader("Authorization", "Bearer $token")
                .build()

            val client = OkHttpClient()

            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    val message = Gson().fromJson(text, ProcessingMessage::class.java)
                    when (message.processingStatus) {
                        "PROCESSING" -> _processingStatus.value = "Обработка..."
                        "COMPLETED" -> {
                            _processingStatus.value = "Готово!"
                            message.base64Image?.let {
                                val imageBytes = Base64.decode(it, Base64.DEFAULT)
                                val bitmap =
                                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                _imageBitmap.value = bitmap
                            }
                        }

                        "FAILED" -> _processingStatus.value = "Ошибка: ${message.errorMessage}"
                        "CANCELED" -> _processingStatus.value = "Обработка отменена"
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    _processingStatus.value = "Ошибка соединения: ${t.message}"
                }
            })
        }
    }
    fun cancelProcessing() {
        webSocket?.send("CANCEL")
        _processingStatus.value = "Отправлен запрос на отмену..."
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.cancel()
    }
}