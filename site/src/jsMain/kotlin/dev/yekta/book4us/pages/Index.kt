package dev.yekta.book4us.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import dev.yekta.book4us.components.layouts.PageLayout
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
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.dom.*
import kotlin.math.abs

// Container that has a tagline and grid on desktop, and just the tagline on mobile
val HeroContainerStyle by ComponentStyle {
    base { Modifier.fillMaxWidth().gap(21.cssRem) }
    Breakpoint.MD { Modifier.margin { top(20.vh) } }
}

// A demo grid that appears on the homepage because it looks good
val HomeGridStyle by ComponentStyle.base {
    Modifier
        .gap(0.5.cssRem)
        .width(70.cssRem)
        .height(18.cssRem)
}

private val GridCellColorVar by StyleVariable<Color>()
val HomeGridCellStyle by ComponentStyle.base {
    Modifier
        .backgroundColor(GridCellColorVar.value())
        .boxShadow(blurRadius = 0.6.cssRem, color = GridCellColorVar.value())
        .borderRadius(1.cssRem)
}

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
    scope.launch { booksState.value = API.getBooks() }

    PageLayout("Home") {
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
            Div(
                Modifier
                    .backgroundColor(Colors.MediumPurple)
                    .padding(1.cssRem)
                    .borderRadius(1.cssRem)
                    .display(DisplayStyle.Flex)
                    .justifyContent(JustifyContent.Center)
                    .alignItems(AlignItems.Center)
                    .fontSize(1.cssRem)
                    .color(Colors.White)
                    .cursor(Cursor.Pointer)
                    .onMouseEnter { GridCellColorVar.value(Colors.DarkOrchid) }
                    .onClick { /*scope.launch { booksState.value = API.getBooks() }*/ }
                    .fontFamily(
                        "system-ui",
                        "-apple-system",
                        "BlinkMacSystemFont",
                        "Segoe UI",
                        "Roboto",
                        "Helvetica Neue",
                        "Arial",
                        "sans-serif"
                    )
                    .toAttrs()
            ) {
                Text("Search")
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
