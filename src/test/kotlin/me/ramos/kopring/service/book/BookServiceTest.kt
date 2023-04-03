package me.ramos.kopring.service.book

import me.ramos.kopring.domain.book.Book
import me.ramos.kopring.domain.book.BookRepository
import me.ramos.kopring.domain.user.User
import me.ramos.kopring.domain.user.UserRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistory
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistoryRepository
import me.ramos.kopring.dto.book.request.BookLoanRequest
import me.ramos.kopring.dto.book.request.BookRequest
import me.ramos.kopring.dto.book.request.BookReturnRequest
import me.ramos.kopring.service.book.BookService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @AfterEach
    fun tearDown() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun saveBookTest() {
        //given
        val request = BookRequest("HTTP 완벽 가이드", "IT")

        //when
        bookService.saveBook(request)

        //then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("HTTP 완벽 가이드")
    }

    @Test
    fun loanBookTest() {
        //given
        bookRepository.save(Book("무엇이 인간을 만드는가", "SOCIETY"))
        val savedUser = userRepository.save(User("Ramos", null))
        val request = BookLoanRequest("Ramos", "무엇이 인간을 만드는가")

        //when
        bookService.loanBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("무엇이 인간을 만드는가")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].isReturn).isFalse
    }

    @Test
    fun loanBookFailTest() {
        //given
        bookRepository.save(Book("무엇이 인간을 만드는가", "SOCIETY"))
        val savedUser = userRepository.save(User("Ramos", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "무엇이 인간을 만드는가", false))
        val request = BookLoanRequest("Ramos", "무엇이 인간을 만드는가")

        //when, then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("이미 대출되어 있는 책입니다")
    }

    @Test
    fun returnBookTest() {
        //given
        bookRepository.save(Book("무엇이 인간을 만드는가", "SOCIETY"))
        val savedUser = userRepository.save(User("Ramos", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "무엇이 인간을 만드는가", false))
        val request = BookReturnRequest("Ramos", "무엇이 인간을 만드는가")

        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].isReturn).isTrue
    }
}