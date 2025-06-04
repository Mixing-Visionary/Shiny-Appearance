package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.repository.CommentRepositoryImpl
import ru.visionary.mixing.shiny_appearance.data.repository.ImageRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.model.CommentResponse
import ru.visionary.mixing.shiny_appearance.domain.model.ImageResponse
import ru.visionary.mixing.shiny_appearance.domain.model.ProtectionType
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(
    private val imageRepository: ImageRepositoryImpl,
    private val commentRepository: CommentRepositoryImpl

) : ViewModel() {
    private val _imageResponse = MutableLiveData<ImageResponse?>()
    val imageResponse: LiveData<ImageResponse?> = _imageResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _comments = MutableStateFlow<List<CommentResponse>>(emptyList())
    val comments: StateFlow<List<CommentResponse>> = _comments

    fun changeProtection(uuid: String, protectionType: ProtectionType) {
        viewModelScope.launch {
            val result = imageRepository.updateImagePrivacy(
                uuid = uuid,
                protection = protectionType
            )
            result.onSuccess {
            }.onFailure {
            }
        }

    }

    fun getImageByUuid(uuid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = imageRepository.getImageByUuid(uuid)
            _isLoading.value = false

            result
                .onSuccess { image ->
                    _imageResponse.value = image
                    _error.value = null
                }
                .onFailure { exception ->
                    _imageResponse.value = null
                    _error.value = exception.message
                }
        }
    }

    fun deleteImageByUuid(uuid: String) {
        viewModelScope.launch {

            imageRepository.deleteImageByUuid(uuid)

        }
    }

    fun postComment(
        uuid: String,
        comment: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val result = commentRepository.postComment(uuid, comment)
            result
                .onSuccess {
                    onSuccess()
                }
                .onFailure { exception ->
                    onError(exception.message ?: "Ошибка при отправке комментария")
                }
        }
    }

    fun getComments(uuid: String, page: Int = 0, size: Int = 30) {
        viewModelScope.launch {
            val result = commentRepository.getComments(uuid, size, page)
            result
                .onSuccess {
                    _comments.value = it
                }
                .onFailure {
                    _error.value = it.message
                }
        }
    }
}