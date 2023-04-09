package me.ramos.kopring.service.book

import me.ramos.kopring.domain.book.Book
import me.ramos.kopring.domain.book.BookRepository
import me.ramos.kopring.domain.user.UserRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistoryRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanStatus
import me.ramos.kopring.dto.book.request.BookLoanRequest
import me.ramos.kopring.dto.book.request.BookRequest
import me.ramos.kopring.dto.book.request.BookReturnRequest
import me.ramos.kopring.dto.book.response.BookStatResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    fun saveBook(request: BookRequest) {
        bookRepository.save(Book(request.name, request.type))
    }

    fun loanBook(request: BookLoanRequest) {
        if (userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) {
            throw IllegalArgumentException("이미 대출되어 있는 책입니다")
        }

        val book = bookRepository.findByName(request.bookName) ?: throw IllegalArgumentException()
        val user = userRepository.findByName(request.userName) ?: throw IllegalArgumentException()

        user.loanBook(book)
    }

    fun returnBook(request: BookReturnRequest) {
        val user =
            userRepository.findByName(request.userName) ?: throw IllegalArgumentException()

        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun countLoanedBook(): Int {
        return userLoanHistoryRepository.countByStatus(UserLoanStatus.LOANED).toInt()
    }

    @Transactional(readOnly = true)
    fun getBookStatistics(): List<BookStatResponse> {
        return bookRepository.getStats()
    }
}