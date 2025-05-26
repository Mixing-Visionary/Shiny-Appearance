package ru.visionary.mixing.shiny_appearance.domain.model

data class UserResponse(
    val userId: Int,
    val nickname: String,
    val email: String?,
    val description: String?,
    val follows: Int?,
    val followers: Int?,
    val likes: Int?,
    val subscription: SubscriptionResponse?,
    val subscriptionEnd: String?,
    val avatarUuid: String?,
    val isFollow: Boolean?
)
