package dev.yekta.book4us.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("author")
    val author: String,
    @SerialName("cover_image")
    val coverImage: String,
    @SerialName("description")
    val description: String,
    @SerialName("genre")
    val genre: List<String>,
    @SerialName("id")
    val id: Int,
    @SerialName("publication_year")
    val publicationYear: Int,
    @SerialName("title")
    val title: String
)
