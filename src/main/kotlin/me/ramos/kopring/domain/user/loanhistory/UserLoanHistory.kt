package me.ramos.kopring.domain.user.loanhistory

import me.ramos.kopring.domain.user.User
import javax.persistence.*

@Entity
@Table(name = "user_loan_histories")
class UserLoanHistory(
    @ManyToOne
    val user: User,

    val bookName: String,

    @Enumerated(EnumType.STRING)
    var status: UserLoanStatus = UserLoanStatus.LOANED,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    val isReturn: Boolean
        get() = this.status == UserLoanStatus.RETURNED

    fun doReturn() {
        this.status = UserLoanStatus.RETURNED
    }

    companion object {
        fun fixture(
            user: User,
            bookName: String = "오브젝트",
            status: UserLoanStatus = UserLoanStatus.LOANED,
        ): UserLoanHistory {
            return UserLoanHistory(
                user = user,
                bookName = bookName,
                status = status,
            )
        }
    }
}