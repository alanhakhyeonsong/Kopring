package me.ramos.kopring.domain.user.loanhistory

import me.ramos.kopring.domain.user.User
import javax.persistence.*

@Entity
@Table(name = "user_loan_histories")
class UserLoanHistory(
    @ManyToOne
    val user: User,

    val bookName: String,

    var isReturn: Boolean,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    fun doReturn() {
        this.isReturn = true
    }
}