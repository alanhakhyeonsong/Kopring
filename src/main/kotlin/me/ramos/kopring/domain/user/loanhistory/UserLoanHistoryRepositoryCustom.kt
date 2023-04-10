package me.ramos.kopring.domain.user.loanhistory

interface UserLoanHistoryRepositoryCustom {

    fun findByBookNameAndStatus(bookName: String, status: UserLoanStatus? = null): UserLoanHistory?

    fun findAllByStatus(userLoanStatus: UserLoanStatus): List<UserLoanHistory>

    fun countByStatus(status: UserLoanStatus): Long
}