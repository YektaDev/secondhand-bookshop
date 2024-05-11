package dev.yekta.book4us.model

sealed interface BookLoadingState {
    data object Loading : BookLoadingState
    data class LoadFailed(val message: String) : BookLoadingState
    data class Loaded(val books: List<Book>) : BookLoadingState
    data class Searching(val books: List<Book>) : BookLoadingState
    data class Searched(val books: List<Book>, val filteredBooks: List<Book>) : BookLoadingState
}
