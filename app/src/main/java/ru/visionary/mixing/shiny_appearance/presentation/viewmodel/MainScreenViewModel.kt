package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.repository.FeedRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.util.getImageUrl
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val feedRepository: FeedRepositoryImpl
) : ViewModel() {

    private val _images = MutableStateFlow<List<DisplayImage>>(emptyList())
    val images: StateFlow<List<DisplayImage>> = _images

    private var publicPage = 0
    private var isLoadingMorePublic = false
    private var isLastPagePublic = false

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        refresh("new")
    }

    fun refresh(sort: String) {
        viewModelScope.launch {
            _images.value = emptyList()
            publicPage = 0
            isLastPagePublic = false
            loadNextPagePublic(sort)
        }
    }

    fun loadNextPagePublic(sort: String) {
        if (isLoadingMorePublic || isLastPagePublic) return

        isLoadingMorePublic = true
        viewModelScope.launch {
            try {
                val response = feedRepository.getFeed(
                    size = 30,
                    page = publicPage,
                    sort = sort
                )
                if (response.isSuccess) {
                    val newImages = response.getOrNull()?.images ?: emptyList()
                    if (newImages.isEmpty()) {
                        isLastPagePublic = true
                    } else {
                        _images.value += newImages.map {
                            DisplayImage(uuid = it.uuid, url = getImageUrl(it.uuid))
                        }
                        publicPage++
                    }
                } else {
                    _errorMessage.value = "Ошибка загрузки публичных публикаций"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сети: ${e.message}"
            }
            isLoadingMorePublic = false
        }
    }

}
