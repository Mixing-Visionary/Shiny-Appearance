package ru.visionary.mixing.shiny_appearance.domain.repository

import ru.visionary.mixing.shiny_appearance.domain.model.DisplayImage
import ru.visionary.mixing.shiny_appearance.domain.model.ProfileImagesResponse

interface FeedRepository {
    suspend fun getFeed(sort: String, size: Int, page: Int): Result<ProfileImagesResponse>
}
