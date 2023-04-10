package me.ramos.kopring.domain.user.loanhistory

import com.querydsl.jpa.impl.JPAQueryFactory
import me.ramos.kopring.domain.user.loanhistory.QUserLoanHistory.userLoanHistory

class UserLoanHistoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
): UserLoanHistoryRepositoryCustom {

    override fun findByBookNameAndStatus(bookName: String, status: UserLoanStatus?): UserLoanHistory? {
        return queryFactory.select(userLoanHistory)
            .from(userLoanHistory)
            .where(
                userLoanHistory.bookName.eq(bookName),
                status?.let { userLoanHistory.status.eq(status) }
            )
            .limit(1)
            .fetchOne()
    }

    override fun findAllByStatus(userLoanStatus: UserLoanStatus): List<UserLoanHistory> {
        return queryFactory.select(userLoanHistory)
            .from(userLoanHistory)
            .where(
                userLoanHistory.status.eq(userLoanStatus)
            )
            .fetch()
    }

    override fun countByStatus(status: UserLoanStatus): Long {
        return queryFactory.select(userLoanHistory.count())
            .from(userLoanHistory)
            .where(
                userLoanHistory.status.eq(status)
            )
            .fetchOne() ?: 0L
    }
}