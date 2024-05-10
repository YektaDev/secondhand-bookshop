package dev.yekta.book4us.data

import dev.yekta.book4us.model.Book
import dev.yekta.book4us.model.BookLoadingState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

object API {
    private const val API_URL = "https://freetestapi.com/api/v1/books"

    val client = HttpClient()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
    }

    suspend fun getBooks(): BookLoadingState = runCatching {
        val body = client.get(API_URL).bodyAsText()
        json.decodeFromString<List<Book>>(body)
    }.fold(
        onSuccess = { BookLoadingState.Success(it) },
        onFailure = { BookLoadingState.Error("Failed to load books from the server \uD83D\uDE41 (${it.message})") }
    )
}