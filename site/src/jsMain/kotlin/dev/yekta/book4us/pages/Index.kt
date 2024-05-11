package dev.yekta.book4us.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.JustifyItems
import com.varabyte.kobweb.compose.css.JustifySelf
import com.varabyte.kobweb.compose.css.accentColor
import com.varabyte.kobweb.compose.css.boxShadow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
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
import dev.yekta.book4us.components.widgets.Button
import dev.yekta.book4us.data.API
import dev.yekta.book4us.model.BookLoadingState
import dev.yekta.book4us.model.BookLoadingState.Loading
import dev.yekta.book4us.model.BookLoadingState.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val booksState: MutableStateFlow<BookLoadingState> = MutableStateFlow(Loading)
private val scope = CoroutineScope(Dispatchers.Default)

@Composable
private fun String.rememberImaginaryPrice() = remember { "$" + abs(hashCode() % 76 + 24).toString() }

@Composable
fun SearchBox(modifier: Modifier = Modifier, hint: String, onSearch: (String) -> Unit = {}) {
    Div(modifier.toAttrs()) {
        var searchInput by remember { mutableStateOf("") }
        LaunchedEffect(searchInput) { onSearch(searchInput) }
        SearchInput(searchInput) {
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
            onInput { searchInput = it.value }
            placeholder(hint)
        }
    }
}

val SearchContainerStyle by ComponentStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .fillMaxWidth()
            .gap(1.cssRem)
            .justifyItems(JustifyItems.Stretch)
            .margin(bottom = 2.cssRem)
    }
    Breakpoint.MD { Modifier.flexDirection(FlexDirection.Row) }
}

@Page
@Composable
fun HomePage() {
    var isSearchVisible by remember { mutableStateOf(false) }
    var minPrice by remember { mutableStateOf(1) }
    var maxPrice by remember { mutableStateOf(100) }

    scope.launch { booksState.value = API.getBooks() }

    PageLayout("Home") {
        when (isSearchVisible) {
            false -> Div(Modifier.margin(bottom = 2.cssRem).toAttrs()) {
                Button("Looking for Something?") { isSearchVisible = true }
            }

            true -> {
                Div(SearchContainerStyle.toAttrs()) {
                    SearchBox(Modifier.fillMaxWidth(), "Search in title & description...") { query ->
//            scope.launch { booksState.value = API.getBooks(query) }
                    }
                    SearchBox(Modifier.fillMaxWidth(), "Search in author...") { query ->
//            scope.launch { booksState.value = API.getBooks(query) }
                    }
                    SearchBox(Modifier.fillMaxWidth(), "Search in genres...") { query ->
//            scope.launch { booksState.value = API.getBooks(query) }
                    }
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
                                marginBottom(2.cssRem)
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
                                marginBottom(2.cssRem)
                            }
                        }
                    }
                    Button("Search") {

                    }
                }
            }
        }


        val state by booksState.collectAsState()
        when (state) {
            is Loading -> {
                Div { Text("Loading...") }
            }

            is BookLoadingState.Error -> {
                Div {
                    Text((state as? Error)?.message?.let { "Error: $it" }
                        ?: "There was an error loading the books. Please check your connection and try again.")
                }
            }

            is Success -> {
                Column(Modifier.gap(1.cssRem)) {
                    (state as Success).books.forEach { book ->
                        Box(
                            modifier = Modifier
                                .boxShadow(
                                    offsetY = 0.125.cssRem,
                                    blurRadius = 0.25.cssRem,
                                    spreadRadius = .05.cssRem,
                                    color = Colors.Black.copy(alpha = 15)
                                )
                                .borderRadius(1.5.cssRem)
                                .backgroundColor(Colors.White)
                                .padding(all = 1.cssRem)
                                .gap(1.5.cssRem)
                                .maxWidth(35.cssRem)
                                .weight(1)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Img(
                                    src = book.coverImage,
                                    attrs = Modifier.borderRadius(1.cssRem).width(8.cssRem).height(12.cssRem).toAttrs()
                                )
                                Column(Modifier.padding(left = 1.cssRem)) {
                                    Div(
                                        Modifier
                                            .fillMaxWidth()
                                            .display(DisplayStyle.Flex)
                                            .alignItems(AlignItems.Center)
                                            .margin(bottom = .5.cssRem)
                                            .toAttrs()
                                    ) {
                                        SpanText(
                                            book.title,
                                            Modifier
                                                .fontWeight(200)
                                                .fontSize(1.25.cssRem)
                                                .textShadow(
                                                    1.px,
                                                    1.px,
                                                    blurRadius = 0.08.cssRem,
                                                    color = Colors.Gray.copy(alpha = 50)
                                                )
                                                .fillMaxWidth()
                                                .padding(right = 1.cssRem)
                                                .flexGrow(1)
                                        )
                                        Spacer()
                                        SpanText(
                                            book.title.rememberImaginaryPrice(),
                                            Modifier
                                                .color(Colors.RebeccaPurple)
                                                .padding(right = .5.cssRem)
                                                .fontSize(1.2.cssRem)
                                                .fontWeight(200)
                                                .letterSpacing(0.1.cssRem)
                                                .justifySelf(JustifySelf.SelfEnd)
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        book.genre.forEach {
                                            SpanText(
                                                it,
                                                Modifier
                                                    .margin(leftRight = .25.cssRem)
                                                    .fontSize(0.7.cssRem)
                                                    .backgroundColor(Colors.MediumPurple)
                                                    .borderRadius(.5.cssRem)
                                                    .color(Colors.White)
                                                    .padding(leftRight = .25.cssRem)
                                                    .flexWrap(FlexWrap.Nowrap)
                                            )
                                        }
                                    }
                                    Span(Modifier.fillMaxWidth().padding(topBottom = 1.cssRem).toAttrs()) {
                                        SpanText(book.author, Modifier.color(Colors.CornflowerBlue))
                                        SpanText(" - ", Modifier.color(Colors.Gray.copy(alpha = 100)))
                                        SpanText(book.publicationYear, Modifier.color(Colors.Gray.copy(alpha = 150)))
                                    }
                                    SpanText(book.description, Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchPanel() {

}
