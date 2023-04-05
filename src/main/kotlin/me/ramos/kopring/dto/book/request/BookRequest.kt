package me.ramos.kopring.dto.book.request

import me.ramos.kopring.domain.book.BookType

data class BookRequest(val name: String, val type: BookType)