package dev.yekta.book4us.model

sealed interface BookLoadingState {
    data object Loading : BookLoadingState
    data class Success(val books: List<Book>) : BookLoadingState
    data class Error(val message: String) : BookLoadingState
}