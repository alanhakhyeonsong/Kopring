package me.ramos.kopring.domain.user

import me.ramos.kopring.config.TestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(TestConfig::class)
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository,
) {

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun save() {
        //given
        val user = User("Ramos", 28)

        //when
        val savedUser = userRepository.save(user)

        //then
        assertThat(savedUser.name).isEqualTo(user.name)
        assertThat(savedUser.age).isEqualTo(user.age)
    }

    @Test
    fun findByName() {
        //given
        val name = "Ramos"
        val age = 28
        val user = User(name, age)
        val savedUser = userRepository.save(user)

        //when
        val result = userRepository.findByName(name)

        //then
        assertThat(result!!.name).isEqualTo(savedUser.name)
        assertThat(result.age).isEqualTo(savedUser.age)
        assertThat(result.id).isEqualTo(savedUser.id)
    }

    @Test
    fun findAllWithHistories() {
        //given
        val name = "Ramos"
        val age = 28
        val user = User(name, age)
        userRepository.save(user)

        //when
        val results = userRepository.findAllWithHistories()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].userLoanHistories).isEmpty()
    }
}