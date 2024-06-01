package dev.yekta.book4us.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.get
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Api
fun books(ctx: ApiContext) {
    val data = ctx.data.get<BookDB>()!!.getAll()
    val response = Json.encodeToString(data)
    ctx.res.setBodyText(response)
}
