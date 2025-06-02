package ru.visionary.mixing.shiny_appearance.data.repository

import ru.visionary.mixing.shiny_appearance.data.remote.api.FeedService
import ru.visionary.mixing.shiny_appearance.domain.model.ProfileImagesResponse
import ru.visionary.mixing.shiny_appearance.domain.repository.FeedRepository
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val api: FeedService
) : FeedRepository {

    override suspend fun getFeed(
        sort: String,
        size: Int,
        page: Int
    ): Result<ProfileImagesResponse> {
        return try {
            val response = api.getFeed(sort, size, page)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
