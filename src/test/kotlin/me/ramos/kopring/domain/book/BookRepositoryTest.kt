package me.ramos.kopring.domain.book

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
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
        val book = Book(bookName)

        //when
        val savedBook = bookRepository.save(book)

        //then
        assertThat(savedBook.name).isEqualTo(bookName)
        assertThat(savedBook.id).isNotNull
    }

    @Test
    fun findByName() {
        //given
        val bookName = "HTTP 완벽 가이드"
        val book = Book(bookName)
        val savedBook = bookRepository.save(book)

        //when
        val result = bookRepository.findByName(bookName)

        //then
        assertThat(result.get().name).isEqualTo(bookName)
        assertThat(result.get().id).isEqualTo(savedBook.id)
    }
}