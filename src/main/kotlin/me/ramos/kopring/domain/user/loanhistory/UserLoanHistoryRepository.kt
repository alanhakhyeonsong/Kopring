package me.ramos.kopring.domain.user.loanhistory

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long> {

    fun findByBookNameAndIsReturn(bookName: String, isReturn: Boolean): Optional<UserLoanHistory>
}