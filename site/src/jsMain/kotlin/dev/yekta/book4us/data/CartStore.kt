package dev.yekta.book4us.data

import dev.yekta.book4us.model.Book
import kotlinx.browser.localStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CartStore {
    private val cart = mutableSetOf<Book>().apply {
        val storedCart = localStorage.getItem("currentCart")
        if (!storedCart.isNullOrBlank()) {
            addAll(Json.decodeFromString<Set<Book>>(storedCart))
        }
    }

    fun get() = cart.toList()

    fun add(book: Book) {
        cart.add(book)
        save()
    }

    fun remove(book: Book) {
        cart.remove(book)
        save()
    }

    fun clear() {
        cart.clear()
        save()
    }

    fun contains(book: Book): Boolean = cart.contains(book)

    private fun save() {
        localStorage.setItem("currentCart", Json.encodeToString<Set<Book>>(cart))
    }
}
