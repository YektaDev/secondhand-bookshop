package dev.yekta.book4us.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.autoLength
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.icons.fa.FaCartShopping
import com.varabyte.kobweb.silk.components.icons.fa.FaFaceSmile
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.text.SpanText
import dev.yekta.book4us.components.layouts.PageLayout
import dev.yekta.book4us.components.modifier.systemFontFamily
import dev.yekta.book4us.components.widgets.BookItem
import dev.yekta.book4us.components.widgets.Button
import dev.yekta.book4us.components.widgets.price
import dev.yekta.book4us.data.CartStore
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle.Companion.Flex
import org.jetbrains.compose.web.css.FlexDirection.Companion.Column
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.*

@Page
@Composable
fun CartPage() {
    PageLayout("Home") {
        Column(Modifier.gap(1.cssRem).maxWidth(35.cssRem)) {
            H2(Modifier.systemFontFamily().toAttrs()) {
                FaCartShopping(Modifier.margin(right = 1.cssRem))
                Text("Your Cart")
            }
            SpanText("You have ${CartStore.get().size} items in your cart.")
            Hr()

            var showThanks by remember { mutableStateOf(false) }
            LaunchedEffect(showThanks) {
                delay(5000)
                showThanks = false
            }
            var cart by remember { mutableStateOf(CartStore.get()) }
            cart.forEach { item ->
                BookItem(
                    book = item,
                    isSelected = true,
                    onSelectClick = {
                        CartStore.remove(item)
                        cart = cart - item
                    },
                )
            }
            if (cart.isNotEmpty()) {
                Div(
                    Modifier
                        .padding(1.cssRem)
                        .backgroundColor(Colors.RebeccaPurple.copy(alpha = 50))
                        .borderRadius(1.cssRem)
                        .display(Flex)
                        .flexDirection(Column)
                        .margin(top = 1.cssRem, left = autoLength, right = autoLength)
                        .gap(1.cssRem)
                        .toAttrs()
                ) {
                    Span(Modifier.toAttrs()) {
                        SpanText("Total: ")
                        SpanText(
                            text = "\$${cart.sumOf { it.price }}",
                            modifier = Modifier
                                .margin(left = 1.cssRem)
                                .color(Colors.MediumPurple)
                                .fontSize(1.5.cssRem)
                        )
                    }
                    Div(Modifier.display(Flex).gap(1.cssRem).toAttrs()) {
                        Button("Checkout") {
                            CartStore.clear()
                            cart = emptyList()
                            showThanks = true
                        }

                        Button("Clear Cart") {
                            CartStore.clear()
                            cart = emptyList()
                        }
                    }
                }
            }
            if (showThanks) {
                Div(Modifier.systemFontFamily().display(Flex).gap(1.cssRem).alignItems(AlignItems.Center).toAttrs()) {
                    FaFaceSmile(Modifier.color(Colors.Green), size = IconSize.X4)
                    SpanText("Thanks for shopping with us!", Modifier.color(Colors.Green).fontWeight(FontWeight.Bold))
                }
            }
        }
    }
}
