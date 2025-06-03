package ru.visionary.mixing.shiny_appearance.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.visionary.mixing.shiny_appearance.domain.model.GetCommentsResponse
import ru.visionary.mixing.shiny_appearance.domain.model.SaveCommentRequest

interface CommentService {

    @POST("/api/v1/image/{uuid}/comment")
    suspend fun postComment(
        @Path("uuid") uuid: String,
        @Body request: SaveCommentRequest
    ): Response<Unit>

    @GET("/api/v1/image/{uuid}/comments")
    suspend fun getComments(
        @Path("uuid") uuid: String,
        @Query("size") size: Int,
        @Query("page") page: Int
    ): Response<GetCommentsResponse>
}
