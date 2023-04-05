package me.ramos.kopring.domain.user.loanhistory

import me.ramos.kopring.domain.user.User
import me.ramos.kopring.domain.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserLoanHistoryRepositoryTest @Autowired constructor(
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val userRepository: UserRepository,
) {

    @AfterEach
    fun tearDown() {
        userLoanHistoryRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun save() {
        //given
        val name = "Ramos"
        val user = User(name, 28)
        val savedUser = userRepository.save(user)

        val bookName = "오브젝트"
        val userLoanHistory = UserLoanHistory.fixture(savedUser, bookName)

        //when
        val savedHistory = userLoanHistoryRepository.save(userLoanHistory)

        //then
        assertThat(savedHistory.bookName).isEqualTo(bookName)
        assertThat(savedHistory.user).isEqualTo(savedUser)
        assertThat(savedHistory.status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    fun findByBookNameAndStatus() {
        //given
        val name = "Ramos"
        val user = User(name, 28)
        val savedUser = userRepository.save(user)

        val bookName = "오브젝트"
        val userLoanHistory = UserLoanHistory.fixture(savedUser, bookName)
        userLoanHistoryRepository.save(userLoanHistory)

        //when
        val result =
            userLoanHistoryRepository.findByBookNameAndStatus(bookName, UserLoanStatus.LOANED)

        //then
        assertThat(result!!.bookName).isEqualTo(bookName)
        assertThat(result.user).isEqualTo(savedUser)
        assertThat(result.status).isEqualTo(UserLoanStatus.LOANED)
    }
}