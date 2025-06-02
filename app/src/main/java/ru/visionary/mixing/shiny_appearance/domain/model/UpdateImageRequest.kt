package ru.visionary.mixing.shiny_appearance.domain.model

import com.google.gson.annotations.SerializedName

data class UpdateImageRequest(
    val protection: ProtectionType
)

enum class ProtectionType {
    @SerializedName("public")
    PUBLIC,

    @SerializedName("private")
    PRIVATE
}