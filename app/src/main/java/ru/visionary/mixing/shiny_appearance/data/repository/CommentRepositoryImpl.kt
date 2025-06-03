package ru.visionary.mixing.shiny_appearance.data.repository

import ru.visionary.mixing.shiny_appearance.data.remote.api.CommentService
import ru.visionary.mixing.shiny_appearance.domain.model.CommentResponse
import ru.visionary.mixing.shiny_appearance.domain.model.SaveCommentRequest
import ru.visionary.mixing.shiny_appearance.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
) : CommentRepository {

    override suspend fun postComment(uuid: String, comment: String): Result<Unit> {
        return try {
            val response = commentService.postComment(uuid, SaveCommentRequest(comment))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to post comment: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getComments(uuid: String, size: Int, page: Int): Result<List<CommentResponse>> {
        return try {
            val response = commentService.getComments(uuid, size, page)
            if (response.isSuccessful) {
                Result.success(response.body()?.comments ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch comments: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
