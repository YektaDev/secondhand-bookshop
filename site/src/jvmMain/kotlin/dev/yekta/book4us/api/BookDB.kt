@file:Suppress("LocalVariableName")

package dev.yekta.book4us.api

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.yekta.book4us.Database
import dev.yekta.book4us.model.Book
import java.io.File

class BookDB(path: File = File("data.db")) {
    private val isNew = !path.exists()
    private val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:${path.absolutePath}")

    init {
        Class.forName("org.sqlite.JDBC")

        path.parentFile?.mkdirs()
        if (isNew) {
            Database.Schema.create(driver)
            driver.execute(identifier = null, sql = INITIAL_TEMP_DATA, parameters = 0, binders = null)
            println("Database Initialized!")
        }
    }

    private val database = Database(driver)

    fun getAll() = database.bookQueries.selectAll {
            id: Long,
            author: String,
            cover_image: String,
            description: String,
            publication_year: String,
            title: String,
            genres: String,
        ->
        Book(
            author = author,
            coverImage = cover_image,
            description = description,
            genre = genres.split(","),
            id = id.toInt(),
            publicationYear = publication_year,
            title = title,
        )
    }.executeAsList()
}
