package dev.yekta.book4us.data

import dev.yekta.book4us.model.Book
import dev.yekta.book4us.model.BookLoadingState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.browser.localStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set

object API {
    private const val API_URL = "http://localhost:8080/api/books"
    private val client = HttpClient()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
    }

    suspend fun getBooks(): BookLoadingState {
        val localBooks = localStorage["books"]
        if (!localBooks.isNullOrBlank()) {
            return BookLoadingState.Loaded(Json.decodeFromString<List<Book>>(localBooks))
        }

        return runCatching {
            val body = client.get(API_URL).bodyAsText()
            json.decodeFromString<List<Book>>(body)
        }.fold(
            onSuccess = { BookLoadingState.Loaded(it.also(::save)) },
            onFailure = { BookLoadingState.LoadFailed("Failed to load books from the server \uD83D\uDE41\nPlease check your connection and try again\n(${it.message})") }
        )
    }

    private fun save(books: List<Book>) {
        localStorage["books"] = Json.encodeToString(books)
    }
}