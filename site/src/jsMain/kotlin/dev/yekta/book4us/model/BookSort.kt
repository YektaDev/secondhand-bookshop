package dev.yekta.book4us.model

import kotlinx.serialization.Serializable

@Serializable
enum class BookSort {
    LEAST_EXPENSIVE_FIRST,
    MOST_EXPENSIVE_FIRST,
}