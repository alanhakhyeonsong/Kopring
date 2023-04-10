package me.ramos.kopring.domain.user

import com.querydsl.jpa.impl.JPAQueryFactory
import me.ramos.kopring.domain.user.QUser.user
import me.ramos.kopring.domain.user.loanhistory.QUserLoanHistory.userLoanHistory

class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : UserRepositoryCustom {

    override fun findAllWithHistories(): List<User> {
        return queryFactory.select(user).distinct()
            .from(user)
            .leftJoin(userLoanHistory).on(userLoanHistory.user.id.eq(user.id)).fetchJoin()
            .fetch()
    }
}