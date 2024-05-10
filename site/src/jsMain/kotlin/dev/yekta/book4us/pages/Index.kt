package dev.yekta.book4us.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.layout.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.ColorSchemes
import dev.yekta.book4us.HeadlineTextStyle
import dev.yekta.book4us.SubheadlineTextStyle
import dev.yekta.book4us.components.layouts.PageLayout
import dev.yekta.book4us.data.API
import dev.yekta.book4us.model.BookLoadingState
import dev.yekta.book4us.model.BookLoadingState.Loading
import dev.yekta.book4us.model.BookLoadingState.Success
import dev.yekta.book4us.toSitePalette
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Col
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text

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

@Composable
private fun GridCell(color: Color, row: Int, column: Int, width: Int? = null, height: Int? = null) {
    Div(
        HomeGridCellStyle.toModifier()
            .setVariable(GridCellColorVar, color)
            .gridItem(row, column, width, height)
            .toAttrs()
    )
}

private val booksState: MutableStateFlow<BookLoadingState> = MutableStateFlow(Loading)


private val scope = CoroutineScope(Dispatchers.Default)


@Page
@Composable
fun HomePage() {
    scope.launch { booksState.value = API.getBooks() }

    PageLayout("Home") {
        val state by booksState.collectAsState()
        when (state) {
            Loading -> {
                Div { Text("Loading...") }
            }

            is BookLoadingState.Error -> {
                Div {
                    Text((state as? Error)?.message?.let { "Error: $it" }
                        ?: "There was an issue with getting the books")
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
                                .gap(1.cssRem)
                                .width(30.cssRem)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Img(src = book.coverImage, attrs = Modifier.borderRadius(1.cssRem).width(6.cssRem).toAttrs())
                                Column(Modifier.padding(left = 1.cssRem)) {
                                    SpanText(
                                        book.title,
                                        Modifier
                                            .fontWeight(500)
                                            .fontSize(1.25.cssRem)
                                            .padding(bottom = 1.cssRem)
                                            .textShadow(1.px, 1.px, blurRadius = 0.08.cssRem, color = Colors.Gray.copy(alpha = 50))
                                            .fillMaxWidth()
                                    )
                                    SpanText(book.author, Modifier.fillMaxWidth())
                                    SpanText(book.publicationYear, Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(HeroContainerStyle.toModifier()) {
            Box {
                val sitePalette = ColorMode.current.toSitePalette()

                Column(Modifier.gap(2.cssRem)) {
                    Div(HeadlineTextStyle.toAttrs()) {
                        SpanText(
                            "Use this template as your starting point for ", Modifier.color(
                                when (ColorMode.current) {
                                    ColorMode.LIGHT -> Colors.Black
                                    ColorMode.DARK -> Colors.White
                                }
                            )
                        )
                        SpanText(
                            "Kobweb",
                            Modifier
                                .color(sitePalette.brand.accent)
                                // Use a shadow so this light-colored word is more visible in light mode
                                .textShadow(0.px, 0.px, blurRadius = 0.5.cssRem, color = Colors.Gray)
                        )
                    }

                    Div(SubheadlineTextStyle.toAttrs()) {
                        SpanText("You can read the ")
                        Link("/about", "About")
                        SpanText(" page for more information.")
                    }

                    val ctx = rememberPageContext()
                    Button(onClick = {
                        // Change this click handler with your call-to-action behavior
                        // here. Link to an order page? Open a calendar UI? Play a movie?
                        // Up to you!
                        ctx.router.tryRoutingTo("/about")
                    }, colorScheme = ColorSchemes.Blue) {
                        Text("This could be your CTA")
                    }
                }
            }

            Div(HomeGridStyle
                .toModifier()
                .displayIfAtLeast(Breakpoint.MD)
                .grid {
                    rows { repeat(3) { size(1.fr) } }
                    columns { repeat(5) { size(1.fr) } }
                }
                .toAttrs()
            ) {
                val sitePalette = ColorMode.current.toSitePalette()
                GridCell(sitePalette.brand.primary, 1, 1, 2, 2)
                GridCell(ColorSchemes.Monochrome._600, 1, 3)
                GridCell(ColorSchemes.Monochrome._100, 1, 4, width = 2)
                GridCell(sitePalette.brand.accent, 2, 3, width = 2)
                GridCell(ColorSchemes.Monochrome._300, 2, 5)
                GridCell(ColorSchemes.Monochrome._800, 3, 1, width = 5)
            }
        }
    }
}
