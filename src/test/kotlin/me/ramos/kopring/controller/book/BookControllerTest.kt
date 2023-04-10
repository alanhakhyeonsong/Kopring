package me.ramos.kopring.controller.book

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.ramos.kopring.domain.book.BookType
import me.ramos.kopring.dto.book.request.BookLoanRequest
import me.ramos.kopring.dto.book.request.BookRequest
import me.ramos.kopring.dto.book.request.BookReturnRequest
import me.ramos.kopring.dto.book.response.BookStatResponse
import me.ramos.kopring.service.book.BookService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var bookService: BookService

    @Test
    fun saveBook() {
        //given
        val request = BookRequest("Kotlin In Action", BookType.COMPUTER)
        val json = jacksonObjectMapper().writeValueAsString(request)
        val uri = "/book"

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun loanBook() {
        //given
        val request = BookLoanRequest("Ramos", "Kotlin In Action")
        val json = jacksonObjectMapper().writeValueAsString(request)
        val uri = "/book/loan"

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun returnBook() {
        //given
        val request = BookReturnRequest("Ramos", "Kotlin In Action")
        val json = jacksonObjectMapper().writeValueAsString(request)
        val uri = "/book/return"

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun countLoanedBook() {
        //given
        val uri = "/book/loan"

        Mockito.`when`(bookService.countLoanedBook()).thenReturn(3)

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun getBookStatistics() {
        //given
        val uri = "/book/stat"
        val response = BookStatResponse(BookType.SCIENCE, 3)

        Mockito.`when`(bookService.getBookStatistics()).thenReturn(listOf(response))

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].type").value(BookType.SCIENCE.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].count").value(3))
            .andDo(MockMvcResultHandlers.print())
    }
}