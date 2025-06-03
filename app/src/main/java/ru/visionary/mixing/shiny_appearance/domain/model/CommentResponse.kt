package ru.visionary.mixing.shiny_appearance.domain.model

data class CommentResponse(
    val authorId: Long,
    val authorNickname: String,
    val authorAvatarUuid: String,
    val authorActive: Boolean,
    val commentId: Long,
    val comment: String,
    val createdAt: String
)