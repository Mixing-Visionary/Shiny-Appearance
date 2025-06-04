package ru.visionary.mixing.shiny_appearance.domain.repository

import ru.visionary.mixing.shiny_appearance.domain.model.CommentResponse

interface CommentRepository {
    suspend fun postComment(uuid: String, comment: String): Result<Unit>
    suspend fun getComments(uuid: String, size: Int, page: Int): Result<List<CommentResponse>>
}
