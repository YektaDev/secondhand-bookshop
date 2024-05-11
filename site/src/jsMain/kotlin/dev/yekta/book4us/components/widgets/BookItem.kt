package dev.yekta.book4us.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.JustifySelf
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaCartPlus
import com.varabyte.kobweb.silk.components.text.SpanText
import dev.yekta.book4us.model.Book
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Span
import kotlin.math.abs

@Composable
private fun String.rememberImaginaryPrice() = remember { "$" + abs(hashCode() % 76 + 24).toString() }

@Composable
fun ColumnScope.BookItem(book: Book, isSelected: Boolean, onSelectClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }

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
            .onMouseEnter { isHovered = true }
            .onMouseLeave { isHovered = false }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.maxWidth(8.cssRem).minWidth(8.cssRem).maxHeight(12.cssRem).minHeight(12.cssRem)) {
                Img(src = book.coverImage, attrs = Modifier.borderRadius(1.cssRem).fillMaxSize().toAttrs())
                if (isHovered || isSelected) {
                    var btnColor by remember { mutableStateOf(Colors.Purple) }
                    FaCartPlus(
                        Modifier
                            .margin(.25.cssRem)
                            .padding(0.5.cssRem)
                            .backgroundColor(btnColor)
                            .color(Colors.White)
                            .borderRadius(1.cssRem)
                            .cursor(Cursor.Pointer)
                            .onClick { onSelectClick() }
                            .onMouseEnter { btnColor = Colors.DarkOrchid }
                            .onMouseLeave { btnColor = Colors.Purple }
                    )
                }
            }
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