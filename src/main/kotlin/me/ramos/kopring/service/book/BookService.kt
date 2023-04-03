package me.ramos.kopring.service.book

import me.ramos.kopring.domain.book.Book
import me.ramos.kopring.domain.book.BookRepository
import me.ramos.kopring.domain.user.UserRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistoryRepository
import me.ramos.kopring.dto.book.request.BookLoanRequest
import me.ramos.kopring.dto.book.request.BookRequest
import me.ramos.kopring.dto.book.request.BookReturnRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @Transactional
    fun saveBook(request: BookRequest) {
        bookRepository.save(Book(request.name, request.type))
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        if (userLoanHistoryRepository.findByBookNameAndIsReturn(request.bookName, false) != null) {
            throw IllegalArgumentException("이미 대출되어 있는 책입니다")
        }

        val book = bookRepository.findByName(request.bookName) ?: throw IllegalArgumentException()
        val user = userRepository.findByName(request.userName) ?: throw IllegalArgumentException()

        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user =
            userRepository.findByName(request.userName) ?: throw IllegalArgumentException()

        user.returnBook(request.bookName)
    }
}