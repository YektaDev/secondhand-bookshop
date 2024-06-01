package dev.yekta.book4us.api

import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext

@InitApi
fun initDatabase(ctx: InitApiContext) {
    ctx.data.add<BookDB>(BookDB())
}
