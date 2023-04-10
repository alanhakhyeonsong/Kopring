package me.ramos.kopring.domain.book

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import me.ramos.kopring.domain.book.QBook.book
import me.ramos.kopring.dto.book.response.BookStatResponse

class BookRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
): BookRepositoryCustom {

    override fun getStats(): List<BookStatResponse> {
        return queryFactory.select(
            Projections.constructor(
            BookStatResponse::class.java,
            book.type,
            book.id.count()
        ))
            .from(book)
            .groupBy(book.type)
            .fetch()
    }
}