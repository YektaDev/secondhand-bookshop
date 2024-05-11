package dev.yekta.book4us.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.JustifyItems
import com.varabyte.kobweb.compose.css.accentColor
import com.varabyte.kobweb.compose.css.boxShadow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import dev.yekta.book4us.components.layouts.PageLayout
import dev.yekta.book4us.components.widgets.BookItem
import dev.yekta.book4us.components.widgets.Button
import dev.yekta.book4us.data.API
import dev.yekta.book4us.data.CartStore
import dev.yekta.book4us.data.Search
import dev.yekta.book4us.model.BookLoadingState
import dev.yekta.book4us.model.BookLoadingState.*
import dev.yekta.book4us.model.BookSort
import dev.yekta.book4us.model.BookSort.LEAST_EXPENSIVE_FIRST
import dev.yekta.book4us.model.BookSort.MOST_EXPENSIVE_FIRST
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.DisplayStyle.Companion.Flex
import org.jetbrains.compose.web.dom.*
import kotlin.math.max
import kotlin.math.min

private val apiBooksState: MutableStateFlow<BookLoadingState> = MutableStateFlow(Loading)
private val scope = CoroutineScope(Dispatchers.Default)
private var searchingJob: Job? = null

@Composable
fun SearchBox(modifier: Modifier = Modifier, hint: String, state: MutableState<String>) {
    Div(modifier.toAttrs()) {
        SearchInput(state.value) {
            style {
                backgroundColor(Colors.White)
                display(DisplayStyle.Block)
                width(100.percent)
                padding(1.cssRem)
                border {
                    width = 0.1.cssRem
                    color = Colors.Gray.copy(alpha = 50)
                    style = LineStyle.Solid
                }
                borderRadius(1.cssRem)
                flexWrap(FlexWrap.Nowrap)
                boxShadow(
                    offsetY = 0.125.cssRem,
                    blurRadius = 0.25.cssRem,
                    spreadRadius = .05.cssRem,
                    color = Colors.Black.copy(alpha = 15)
                )
            }
            onInput { state.value = it.value }
            placeholder(hint)
        }
    }
}

val SearchContainerStyle by ComponentStyle {
    base {
        Modifier
            .display(Flex)
            .flexDirection(FlexDirection.Column)
            .fillMaxWidth()
            .gap(1.cssRem)
            .justifyItems(JustifyItems.Stretch)
            .margin(bottom = 1.cssRem)
    }
    Breakpoint.MD { Modifier.flexDirection(FlexDirection.Row) }
}

@Page
@Composable
fun HomePage() {
    var isSearchVisible by remember { mutableStateOf(false) }
    val titleOrDescription = remember { mutableStateOf("") }
    val author = remember { mutableStateOf("") }
    val genres = remember { mutableStateOf("") }
    var minPrice by remember { mutableStateOf(1) }
    var maxPrice by remember { mutableStateOf(100) }
    var sort by remember { mutableStateOf(LEAST_EXPENSIVE_FIRST) }

    scope.launch { apiBooksState.value = API.getBooks() }

    LaunchedEffect(titleOrDescription.value, author.value, genres.value, minPrice, maxPrice, sort) {
        withContext(Dispatchers.Main.immediate) {
            when (val state = apiBooksState.value) {
                Loading, is LoadFailed, is Loaded, is Searching -> Unit
                is Searched -> apiBooksState.value = Searching(state.books)
            }
        }
    }

    PageLayout("Home") {
        Box(Modifier.maxWidth(35.cssRem).fillMaxWidth()) {
            Div(SearchContainerStyle.toAttrs()) {
                Button(if (isSearchVisible) "Hide Search Panel" else "Looking for Something?") {
                    isSearchVisible = !isSearchVisible
                }
                Select(
                    attrs = {
                        style {
                            backgroundColor(Colors.White)
                            paddingLeft(1.cssRem)
                            paddingRight(1.cssRem)
                            paddingTop(0.75.cssRem)
                            paddingBottom(0.75.cssRem)
                            flexWrap(FlexWrap.Nowrap)
                            flexGrow(1)
                            flexShrink(0)
                            border {
                                width = 0.1.cssRem
                                color = Colors.Gray.copy(alpha = 50)
                                style = LineStyle.Solid
                            }
                            borderRadius(.5.cssRem)
                            boxShadow(
                                offsetY = 0.125.cssRem,
                                blurRadius = 0.25.cssRem,
                                spreadRadius = .05.cssRem,
                                color = Colors.Black.copy(alpha = 15)
                            )
                        }
                        onChange {
                            sort = it.value?.let { value -> Json.decodeFromString<BookSort>(value) }
                                ?: LEAST_EXPENSIVE_FIRST
                        }
                    }
                ) {
                    val leastExpensive = remember { Json.encodeToString(LEAST_EXPENSIVE_FIRST) }
                    val mostExpensive = remember { Json.encodeToString(MOST_EXPENSIVE_FIRST) }
                    Option(value = leastExpensive) { Text("Least Expensive First") }
                    Option(value = mostExpensive) { Text("Most Expensive First") }
                }
            }
        }
        if (isSearchVisible) {
            Div(
                Modifier
                    .backgroundColor(Colors.RebeccaPurple.copy(alpha = 30))
                    .boxShadow(
                        offsetY = .1.cssRem,
                        blurRadius = .25.cssRem,
                        spreadRadius = .05.cssRem,
                        color = Colors.Black.copy(alpha = 15)
                    )
                    .padding(left = 1.cssRem, top = 1.cssRem, right = 1.cssRem)
                    .margin(top = 1.cssRem, left = 1.cssRem, right = 1.cssRem, bottom = 2.cssRem)
                    .borderRadius(1.cssRem)
                    .fillMaxWidth()
                    .toAttrs()
            ) {
                Div(SearchContainerStyle.toAttrs()) {
                    SearchBox(Modifier.fillMaxWidth(), "Search in title/description...", titleOrDescription)
                    SearchBox(Modifier.fillMaxWidth(), "Search in author...", author)
                    SearchBox(Modifier.fillMaxWidth(), "Search in genres...", genres)
                }
                Div(SearchContainerStyle.toAttrs()) {
                    Div(Modifier.gap(1.cssRem).flexGrow(1).toAttrs()) {
                        SpanText("Min Price: ", Modifier.color(Colors.RebeccaPurple.copy(alpha = 180)))
                        SpanText(
                            " \$$minPrice",
                            Modifier.color(Colors.RebeccaPurple).fontWeight(600).fontSize(1.25.cssRem)
                        )
                        RangeInput(value = minPrice, min = 1, max = 100, step = 1) {
                            onInput { minPrice = min(maxPrice - 1, it.value?.toInt() ?: 0) }
                            style {
                                width(100.percent)
                                height(1.cssRem)
                                backgroundColor(Colors.White)
                                display(DisplayStyle.Block)
                                borderRadius(1.cssRem)
                                accentColor(Colors.MediumPurple)
                                marginBottom(1.cssRem)
                            }
                        }
                    }
                    Div(Modifier.gap(1.cssRem).flexGrow(1).toAttrs()) {
                        SpanText("Max Price: ", Modifier.color(Colors.RebeccaPurple.copy(alpha = 180)))
                        SpanText(
                            " \$$maxPrice",
                            Modifier.color(Colors.RebeccaPurple).fontWeight(600).fontSize(1.25.cssRem)
                        )
                        RangeInput(value = maxPrice, min = 1, max = 100, step = 1) {
                            onInput { maxPrice = max(minPrice + 1, it.value?.toInt() ?: 0) }
                            style {
                                width(100.percent)
                                height(1.cssRem)
                                backgroundColor(Colors.White)
                                display(DisplayStyle.Block)
                                borderRadius(1.cssRem)
                                accentColor(Colors.MediumPurple)
                                marginBottom(1.cssRem)
                            }
                        }
                    }
                }
            }
        }

        when (val state = apiBooksState.collectAsState().value) {
            is Loading -> Div { Text("Loading...") }
            is Searching -> {
                LaunchedEffect(titleOrDescription.value, author.value, genres.value, minPrice, maxPrice, sort) {
                    searchingJob?.cancel()
                    searchingJob = scope.launch {
                        val result = Search.search(
                            books = state.books,
                            titleOrDescription = titleOrDescription.value,
                            author = author.value,
                            genres = genres.value,
                            minPrice = minPrice,
                            maxPrice = maxPrice,
                            sort = sort,
                        )
                        apiBooksState.update { Searched(books = state.books, filteredBooks = result) }
                    }
                }
                Div { Text("Searching...") }
            }

            is LoadFailed -> Div { Text(state.message.let { "Error: $it" }) }
            is Loaded -> {
                LaunchedEffect(Unit) {
                    apiBooksState.value = Searching(state.books)
                }
                Div { Text("Books loaded, starting search...") }
            }

            is Searched -> Column(Modifier.gap(1.cssRem)) {
                state.filteredBooks.forEach { book ->
                    var isBookInCart by remember { mutableStateOf(CartStore.contains(book)) }
                    BookItem(book, isBookInCart) {
                        isBookInCart = !isBookInCart
                        if (isBookInCart) CartStore.add(book)
                        else CartStore.remove(book)
                    }
                }
            }
        }
    }
}

