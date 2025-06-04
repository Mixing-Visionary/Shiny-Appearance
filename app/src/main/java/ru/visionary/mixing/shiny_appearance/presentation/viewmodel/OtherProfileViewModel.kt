package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.repository.FollowRepositoryImpl
import ru.visionary.mixing.shiny_appearance.data.repository.ProfileImagesRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.domain.model.ImageResponse
import ru.visionary.mixing.shiny_appearance.domain.repository.UserRepository
import ru.visionary.mixing.shiny_appearance.util.getImageUrl
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileImagesRepositoryImpl: ProfileImagesRepositoryImpl,
    private val followRepository: FollowRepositoryImpl
) : ViewModel() {

    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> = _userId

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _likes = MutableStateFlow(0)
    val likes: StateFlow<Int> = _likes

    private val _publicImages = MutableStateFlow<List<ImageResponse>>(emptyList())

    private val _publicPosts = MutableStateFlow<List<DisplayImage>>(emptyList())
    val publicPosts: StateFlow<List<DisplayImage>> = _publicPosts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _followResult = MutableStateFlow<Result<Unit>?>(null)
    val followResult: StateFlow<Result<Unit>?> = _followResult

    private var publicPage = 0
    private var isLoadingMorePublic = false
    private var isLastPagePublic = false

    fun followUser(userId: Int) {
        viewModelScope.launch {
            val result = followRepository.followUser(userId)
            _followResult.value = result
        }
    }

    fun refresh(userId: Int) {
        viewModelScope.launch {
            fetchUserData(userId)
            resetAndLoadPublic(userId)
        }
    }

    fun resetAndLoadPublic(userId: Int) {
        publicPage = 0
        isLastPagePublic = false
        _publicImages.value = emptyList()
        _publicPosts.value = emptyList()
        loadNextPagePublic(userId)
    }

    private suspend fun fetchUserData(userId: Int) {
        _isLoading.value = true

        val userResult = userRepository.getUser(userId)
        if (userResult.isSuccess) {
            val user = userResult.getOrNull()
            _userId.value = user?.userId!!
            _nickname.value = user?.nickname.orEmpty()
            _description.value = user?.description.orEmpty()
            _likes.value = user?.likes ?: 0
            _errorMessage.value = null
        } else {
            _errorMessage.value = userResult.exceptionOrNull()?.message
            _isLoading.value = false
            return
        }

        _isLoading.value = false
    }

    fun loadNextPagePublic(userId: Int) {
        if (isLoadingMorePublic || isLastPagePublic) return

        isLoadingMorePublic = true
        viewModelScope.launch {
            try {
                val response = profileImagesRepositoryImpl.getOtherUserImages(
                    userId = userId,
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
}
