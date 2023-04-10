package me.ramos.kopring.domain.book

import me.ramos.kopring.dto.book.response.BookStatResponse

interface BookRepositoryCustom {

    fun getStats(): List<BookStatResponse>
}