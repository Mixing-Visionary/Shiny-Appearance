package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.repository.ImageRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.model.ImageResponse
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(
    private val imageRepository: ImageRepositoryImpl
) : ViewModel() {
    private val _imageResponse = MutableLiveData<ImageResponse?>()
    val imageResponse: LiveData<ImageResponse?> = _imageResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


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
}