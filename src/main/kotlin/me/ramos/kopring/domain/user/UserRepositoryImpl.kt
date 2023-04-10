package me.ramos.kopring.domain.user

import com.querydsl.jpa.impl.JPAQueryFactory
import me.ramos.kopring.domain.user.QUser.user

class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
): UserRepositoryCustom {

    override fun findAllWithHistories(): List<User> {
        return queryFactory.select(user).distinct()
            .from(user)
            .leftJoin(user.userLoanHistories).fetchJoin()
            .fetch()
    }
}