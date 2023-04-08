package me.ramos.kopring.service.user

import me.ramos.kopring.domain.user.User
import me.ramos.kopring.domain.user.UserRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistory
import me.ramos.kopring.domain.user.loanhistory.UserLoanHistoryRepository
import me.ramos.kopring.domain.user.loanhistory.UserLoanStatus
import me.ramos.kopring.dto.user.request.UserCreateRequest
import me.ramos.kopring.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    @Autowired
    private lateinit var userLoanHistoryRepository: UserLoanHistoryRepository

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUserTest() {
        //given
        val request = UserCreateRequest("Ramos", null)

        //when
        userService.saveUser(request)

        //then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("Ramos")
        assertThat(results[0].age).isNull()
    }

    @Test
    fun getUsersTest() {
        //given
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null),
        ))

        //when
        val results = userService.getUsers()

        //then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    fun updateUserNameTest() {
        //given
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id!!, "B")

        //when
        userService.updateUsername(request)

        //then
        val result = userRepository.findAll()[0]
        assertThat(request.name).isEqualTo("B")
    }

    @Test
    fun deleteUserTest() {
        //given
        userRepository.save(User("A", null))

        //when
        userService.deleteUser("A")

        //then
        assertThat(userRepository.findAll()).hasSize(0)
    }

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다")
    fun getUserLoanHistories_hasNotHistories() {
        //given
        userRepository.save(User("A", null))


        //when
        val results = userService.getUserLoanHistories()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다")
    fun getUserLoanHistories_hasHistories() {
        //given
        val savedUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory(savedUser, "책1", UserLoanStatus.LOANED),
            UserLoanHistory(savedUser, "책2", UserLoanStatus.LOANED),
            UserLoanHistory(savedUser, "책3", UserLoanStatus.RETURNED),
        ))

        //when
        val results = userService.getUserLoanHistories()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name").containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(results[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)
    }
}