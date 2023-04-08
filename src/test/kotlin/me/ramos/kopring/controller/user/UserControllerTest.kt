package me.ramos.kopring.controller.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.ramos.kopring.dto.user.request.UserCreateRequest
import me.ramos.kopring.dto.user.request.UserUpdateRequest
import me.ramos.kopring.dto.user.response.BookHistoryResponse
import me.ramos.kopring.dto.user.response.UserLoanHistoryResponse
import me.ramos.kopring.dto.user.response.UserResponse
import me.ramos.kopring.service.user.UserService
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
import org.springframework.util.LinkedMultiValueMap

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun saveUser() {
        //given
        val request = UserCreateRequest("Ramos", 28)
        val json = jacksonObjectMapper().writeValueAsString(request)
        val uri = "/user"

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
    fun getUsers() {
        //given
        val uri = "/user"

        Mockito.`when`(userService.getUsers()).thenReturn(listOf(UserResponse(1L, "Ramos", 28)))

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").value("Ramos"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].age").value(28))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun updateUsername() {
        //given
        val request = UserUpdateRequest(1L, "Ramos")
        val json = jacksonObjectMapper().writeValueAsString(request)
        val uri = "/user"

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
    fun deleteUser() {
        //given
        val request = LinkedMultiValueMap<String, String>();
        request.add("name", "Ramos");
        val uri = "/user"

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.delete(uri)
                .params(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun getUserLoanHistories() {
        //given
        val uri = "/user/loan"
        val userName = "Ramos"
        val bookName = "객체지향의 사실과 오해"
        val bookHistory = BookHistoryResponse(bookName, true)

        Mockito.`when`(userService.getUserLoanHistories())
            .thenReturn(
                listOf(
                    UserLoanHistoryResponse(
                        userName,
                        listOf(bookHistory)
                    )
                )
            )

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").value(userName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].books[0].name").value(bookHistory.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].books[0].isReturn").value(bookHistory.isReturn))
            .andDo(MockMvcResultHandlers.print())
    }
}