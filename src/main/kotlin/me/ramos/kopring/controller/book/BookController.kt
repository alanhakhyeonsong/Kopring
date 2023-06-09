package me.ramos.kopring.controller.book

import me.ramos.kopring.dto.book.request.BookLoanRequest
import me.ramos.kopring.dto.book.request.BookRequest
import me.ramos.kopring.dto.book.request.BookReturnRequest
import me.ramos.kopring.dto.book.response.BookStatResponse
import me.ramos.kopring.service.book.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/book")
class BookController(
    private val bookService: BookService,
) {

    @PostMapping
    fun saveBook(@RequestBody request: BookRequest) {
        bookService.saveBook(request)
    }

    @PostMapping("/loan")
    fun loanBook(@RequestBody request: BookLoanRequest) {
        bookService.loanBook(request)
    }

    @PutMapping("/return")
    fun returnBook(@RequestBody request: BookReturnRequest) {
        bookService.returnBook(request)
    }

    @GetMapping("/loan")
    fun countLoanedBook(): Int {
        return bookService.countLoanedBook()
    }

    @GetMapping("/stat")
    fun getBookStatistics(): List<BookStatResponse> {
        return bookService.getBookStatistics()
    }
}