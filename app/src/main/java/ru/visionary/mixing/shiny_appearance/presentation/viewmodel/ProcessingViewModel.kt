package ru.visionary.mixing.shiny_appearance.presentation.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.visionary.mixing.shiny_appearance.data.repository.ProcessingRepository
import javax.inject.Inject

@HiltViewModel
class ProcessingViewModel @Inject constructor(
    private val repository: ProcessingRepository,
    private val appContext: Application
) : ViewModel() {

    private val _uuid = MutableStateFlow<String?>(null)
    val uuid: StateFlow<String?> = _uuid.asStateFlow()

    fun processImage(
        uri: Uri,
        style: String,
        strength: Float?,
        onSuccess: (String) -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val uuid = repository.processImage(
                    uri = uri,
                    context = appContext,
                    style = style,
                    strength = strength
                )
                _uuid.value = uuid
                if (uuid != null) {
                    onSuccess(uuid)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError()
            }
        }
    }
}

