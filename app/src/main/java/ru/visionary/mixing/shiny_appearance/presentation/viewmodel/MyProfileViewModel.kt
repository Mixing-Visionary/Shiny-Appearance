package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.repository.ProfileImagesRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.domain.model.ImageResponse
import ru.visionary.mixing.shiny_appearance.domain.repository.UserRepository
import ru.visionary.mixing.shiny_appearance.util.getImageUrl
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileImagesRepositoryImpl: ProfileImagesRepositoryImpl
) : ViewModel() {

    var userId by mutableStateOf(0)
        private set

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _publicImages = MutableStateFlow<List<ImageResponse>>(emptyList())


    private val _privateImages = MutableStateFlow<List<ImageResponse>>(emptyList())

    private val _publicPosts = MutableStateFlow<List<DisplayImage>>(emptyList())
    val publicPosts: StateFlow<List<DisplayImage>> = _publicPosts

    private val _privatePosts = MutableStateFlow<List<DisplayImage>>(emptyList())
    val privatePosts: StateFlow<List<DisplayImage>> = _privatePosts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var publicPage = 0
    private var isLoadingMorePublic = false
    private var isLastPagePublic = false

    private var privatePage = 0
    private var isLoadingMorePrivate = false
    private var isLastPagePrivate = false

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            fetchUserData()
            resetAndLoadPublic()
            resetAndLoadPrivate()
        }
    }


    private suspend fun fetchUserData() {
        _isLoading.value = true

        val userResult = userRepository.getCurrentUser()
        if (userResult.isSuccess) {
            val user = userResult.getOrNull()
            _nickname.value = user?.nickname.orEmpty()
            _description.value = user?.description.orEmpty()
            _errorMessage.value = null
        } else {
            _errorMessage.value = userResult.exceptionOrNull()?.message
            _isLoading.value = false
            return
        }

        _isLoading.value = false
    }

    fun resetAndLoadPublic() {
        publicPage = 0
        isLastPagePublic = false
        _publicImages.value = emptyList()
        _publicPosts.value = emptyList()
        loadNextPagePublic()
    }

    fun loadNextPagePublic() {
        if (isLoadingMorePublic || isLastPagePublic) return

        isLoadingMorePublic = true
        viewModelScope.launch {
            try {
                val response = profileImagesRepositoryImpl.getUserImages(
                    size = 30,
                    page = publicPage,
                    protection = "public"
                )
                if (response.isSuccess) {
                    val newImages = response.getOrNull()?.images ?: emptyList()
                    if (newImages.isEmpty()) {
                        isLastPagePublic = true
                    } else {
                        _publicImages.value += newImages
                        _publicPosts.value += newImages.map {
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

    fun resetAndLoadPrivate() {
        privatePage = 0
        isLastPagePrivate = false
        _privateImages.value = emptyList()
        _privatePosts.value = emptyList()
        loadNextPagePrivate()
    }

    fun loadNextPagePrivate() {
        if (isLoadingMorePrivate || isLastPagePrivate) return

        isLoadingMorePrivate = true
        viewModelScope.launch {
            try {
                val response = profileImagesRepositoryImpl.getUserImages(
                    size = 30,
                    page = privatePage,
                    protection = "private"
                )
                if (response.isSuccess) {
                    val newImages = response.getOrNull()?.images ?: emptyList()
                    if (newImages.isEmpty()) {
                        isLastPagePrivate = true
                    } else {
                        _privateImages.value += newImages
                        _privatePosts.value += newImages.map {
                            DisplayImage(uuid = it.uuid, url = getImageUrl(it.uuid))
                        }
                        privatePage++
                    }
                } else {
                    _errorMessage.value = "Ошибка загрузки приватных публикаций"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сети: ${e.message}"
            }
            isLoadingMorePrivate = false
        }
    }

    private val _updateResult = MutableStateFlow<Result<Unit>?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateUser(nickname: String?, description: String?, password: String?) {
        viewModelScope.launch {
            val result = userRepository.updateCurrentUser( nickname, description, password)
            _updateResult.value = result
        }
    }

    fun deleteCurrentUser(){
        viewModelScope.launch {
            userRepository.deleteCurrentUser()
        }
    }


}
