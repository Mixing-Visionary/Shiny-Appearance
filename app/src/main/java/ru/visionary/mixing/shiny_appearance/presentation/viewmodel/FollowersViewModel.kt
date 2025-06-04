package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.repository.FollowRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.model.UserResponse
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val followRepository: FollowRepositoryImpl
) : ViewModel() {

    private val _followers = MutableStateFlow<List<UserResponse>>(emptyList())
    val followers: StateFlow<List<UserResponse>> = _followers

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 0
    private var isLastPage = false
    private val pageSize = 20

    fun loadNextPage() {
        if (_isLoading.value || isLastPage) return

        _isLoading.value = true
        viewModelScope.launch {
            val result = followRepository.getFollowers(size = pageSize, page = currentPage)
            _isLoading.value = false

            result
                .onSuccess { usersResponse ->
                    val newUsers = usersResponse.users
                    if (newUsers.size < pageSize) {
                        isLastPage = true
                    }
                    _followers.update { it + newUsers }
                    currentPage++
                }
                .onFailure {
                    _error.value = it.message
                }
        }
    }

    fun refreshFollows() {
        currentPage = 0
        isLastPage = false
        _followers.value = emptyList()
        loadNextPage()
    }
}