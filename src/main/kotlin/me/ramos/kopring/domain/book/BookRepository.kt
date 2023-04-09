package me.ramos.kopring.domain.book

import me.ramos.kopring.dto.book.response.BookStatResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BookRepository : JpaRepository<Book, Long> {

    fun findByName(bookName: String): Book?

    @Query("SELECT NEW me.ramos.kopring.dto.book.response.BookStatResponse(b.type, COUNT(b.id))" +
            " FROM Book b GROUP BY b.type")
    fun getStats(): List<BookStatResponse>
}