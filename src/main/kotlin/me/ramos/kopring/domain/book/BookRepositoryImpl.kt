package me.ramos.kopring.domain.book

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import me.ramos.kopring.dto.book.response.BookStatResponse

class BookRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
): BookRepositoryCustom {

    override fun getStats(): List<BookStatResponse> {
        return queryFactory.select(
            Projections.constructor(
            BookStatResponse::class.java,
            QBook.book.type,
            QBook.book.id.count()
        ))
            .from(QBook.book)
            .groupBy(QBook.book.type)
            .fetch()
    }
}