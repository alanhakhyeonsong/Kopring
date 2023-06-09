package me.ramos.kopring.domain.book

import me.ramos.kopring.config.TestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(TestConfig::class)
class BookRepositoryTest @Autowired constructor(
    private val bookRepository: BookRepository,
) {

    @AfterEach
    fun tearDown() {
        bookRepository.deleteAll()
    }

    @Test
    fun save() {
        //given
        val bookName = "HTTP 완벽 가이드"
        val book = Book.fixture(bookName)

        //when
        val savedBook = bookRepository.save(book)

        //then
        assertThat(savedBook.name).isEqualTo(bookName)
        assertThat(savedBook.type).isEqualTo(BookType.COMPUTER)
        assertThat(savedBook.id).isNotNull
    }

    @Test
    fun findByName() {
        //given
        val bookName = "HTTP 완벽 가이드"
        val book = Book.fixture(bookName)
        val savedBook = bookRepository.save(book)

        //when
        val result = bookRepository.findByName(bookName)

        //then
        assertThat(result!!.name).isEqualTo(bookName)
        assertThat(result.type).isEqualTo(BookType.COMPUTER)
        assertThat(result.id).isEqualTo(savedBook.id)
    }

    @Test
    fun getStats() {
        //given
        val bookName = "HTTP 완벽 가이드"
        val book = Book.fixture(bookName)
        bookRepository.save(book)

        //when
        val results = bookRepository.getStats()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].type).isEqualTo(BookType.COMPUTER)
        assertThat(results[0].count).isEqualTo(1)
    }
}