package me.ramos.kopring.dto.book.response

import me.ramos.kopring.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    var count: Int
) {
    fun plusOne() {
        this.count += 1
    }
}