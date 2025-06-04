package ru.visionary.mixing.shiny_appearance.domain.repository

import ru.visionary.mixing.shiny_appearance.domain.model.UsersResponse

interface FollowRepository {
    suspend fun followUser(userId: Int): Result<Unit>

    suspend fun followUserDelete(userId: Int): Result<Unit>

    suspend fun getFollows(size: Int, page: Int): Result<UsersResponse>
    suspend fun getFollowsById(userId: Int, size: Int, page: Int): Result<UsersResponse>

    suspend fun getFollowers(size: Int, page: Int): Result<UsersResponse>


}
