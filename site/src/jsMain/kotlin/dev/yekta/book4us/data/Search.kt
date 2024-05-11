package dev.yekta.book4us.data

import dev.yekta.book4us.components.widgets.price
import dev.yekta.book4us.model.Book
import dev.yekta.book4us.model.BookSort
import dev.yekta.book4us.model.BookSort.LEAST_EXPENSIVE_FIRST
import dev.yekta.book4us.model.BookSort.MOST_EXPENSIVE_FIRST

object Search {
    fun search(
        books: List<Book>,
        titleOrDescription: String,
        author: String,
        genres: String,
        minPrice: Int,
        maxPrice: Int,
        sort: BookSort
    ): List<Book> {
        return search(
            books = books,
            titleOrDescription = titleOrDescription.trim(),
            author = author.trim(),
            allGenres = genres.split(",").mapNotNull { it.trim().lowercase().takeIf(String::isNotEmpty) },
            minPrice = minPrice,
            maxPrice = maxPrice,
            sort = sort,
        )
    }

    private fun search(
        books: List<Book>,
        allGenres: List<String>,
        titleOrDescription: String,
        author: String,
        minPrice: Int,
        maxPrice: Int,
        sort: BookSort,
    ): List<Book> {
        return books
            .asSequence()
            .filterIf(author.isNotBlank()) { it.author.contains(author, ignoreCase = true) }
            .filterIf(allGenres.isNotEmpty()) { book ->
                allGenres.any { genre ->
                    book.genre.firstOrNull { it.trim().contains(genre, ignoreCase = true) } != null
                }
            }
            .filter { it.price in minPrice..maxPrice }
            .filterIf(titleOrDescription.isNotBlank()) {
                it.title.contains(titleOrDescription, ignoreCase = true)
                        || it.description.contains(titleOrDescription, ignoreCase = true)
            }
            .let { seq ->
                when (sort) {
                    LEAST_EXPENSIVE_FIRST -> seq.sortedBy { it.price }
                    MOST_EXPENSIVE_FIRST -> seq.sortedByDescending { it.price }
                }
            }.toList()
    }

    private fun Sequence<Book>.filterIf(condition: Boolean, predicate: (Book) -> Boolean): Sequence<Book> {
        return if (condition) filter(predicate) else this
    }
}