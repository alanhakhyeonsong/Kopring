package me.ramos.kopring.service.book

import me.ramos.kopring.domain.book.Book
import me.ramos.kopring.domain.book.BookRepository
import me.ramos.kopring.domain.book.BookType
import me.ramos.kopring.domain.user.User
import me.ramos.kopring.domain.user.UserRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistory
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistoryRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanStatus
import me.ramos.kopring.dto.book.request.BookLoanRequest
import me.ramos.kopring.dto.book.request.BookRequest
import me.ramos.kopring.dto.book.request.BookReturnRequest
import me.ramos.kopring.dto.book.response.BookStatResponse
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
        val request = BookRequest("HTTP 완벽 가이드", BookType.COMPUTER)

        //when
        bookService.saveBook(request)

        //then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("HTTP 완벽 가이드")
        assertThat(books[0].type).isEqualTo(BookType.COMPUTER)
    }

    @Test
    fun loanBookTest() {
        //given
        bookRepository.save(Book("무엇이 인간을 만드는가", BookType.SOCIETY))
        val savedUser = userRepository.save(User("Ramos", null))
        val request = BookLoanRequest("Ramos", "무엇이 인간을 만드는가")

        //when
        bookService.loanBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("무엇이 인간을 만드는가")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    fun loanBookFailTest() {
        //given
        bookRepository.save(Book("무엇이 인간을 만드는가", BookType.SOCIETY))
        val savedUser = userRepository.save(User("Ramos", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "무엇이 인간을 만드는가"))
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
        bookRepository.save(Book("무엇이 인간을 만드는가", BookType.SOCIETY))
        val savedUser = userRepository.save(User("Ramos", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "무엇이 인간을 만드는가"))
        val request = BookReturnRequest("Ramos", "무엇이 인간을 만드는가")

        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    fun countLoanedBookTest() {
        //given
        val savedUser = userRepository.save(User("Ramos", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "A"),
                UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(savedUser, "C", UserLoanStatus.RETURNED),
            )
        )

        //when
        val result = bookService.countLoanedBook()

        //then
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun getBookStatisticsTest() {
        //given
        bookRepository.saveAll(
            listOf(
                Book.fixture("A", BookType.COMPUTER),
                Book.fixture("B", BookType.COMPUTER),
                Book.fixture("C", BookType.SCIENCE)
            )
        )

        //when
        val results = bookService.getBookStatistics()

        //then
        assertThat(results).hasSize(2)
        assertCount(results, BookType.COMPUTER, 2)
        assertCount(results, BookType.SCIENCE, 1)
    }

    private fun assertCount(results: List<BookStatResponse>, type: BookType, expectedCount: Int) {
        assertThat(results.first { it.type == type }.count).isEqualTo(expectedCount)
    }
}