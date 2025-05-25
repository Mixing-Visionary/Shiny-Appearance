package ru.visionary.mixing.shiny_appearance.domain.model

data class ProcessingMessage(
    val base64Image: String?,
    val processingStatus: String,
    val errorCode: Int?,
    val errorMessage: String?
)