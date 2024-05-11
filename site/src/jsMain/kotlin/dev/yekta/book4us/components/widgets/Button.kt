package dev.yekta.book4us.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.autoLength
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun Button(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    var color by remember { mutableStateOf(Colors.MediumPurple) }
    Div(
        modifier
            .backgroundColor(color)
            .padding(topBottom = .5.cssRem, leftRight = 2.cssRem)
            .borderRadius(.5.cssRem)
            .display(DisplayStyle.Flex)
            .margin(topBottom = autoLength)
            .justifyContent(JustifyContent.Center)
            .alignItems(AlignItems.Center)
            .fontSize(1.cssRem)
            .color(Colors.White)
            .cursor(Cursor.Pointer)
            .onMouseEnter { color = Colors.DarkOrchid }
            .onMouseLeave { color = Colors.MediumPurple }
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
            .onClick { onClick() }
            .toAttrs()
    ) { Text(text) }
}