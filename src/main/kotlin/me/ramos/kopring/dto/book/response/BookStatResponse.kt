package me.ramos.kopring.dto.book.response

import me.ramos.kopring.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    val count: Int
) {
}