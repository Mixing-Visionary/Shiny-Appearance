package ru.visionary.mixing.shiny_appearance.domain.model

data class ImageResponse(
    val uuid: String,
    val protection: String,
    val authorId: Int,
    val authorNickname: String,
    val authorAvatarUuid: String?,
    val likes: Int
)