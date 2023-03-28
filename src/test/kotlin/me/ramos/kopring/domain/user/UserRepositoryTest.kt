package me.ramos.kopring.domain.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
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
        assertThat(result.get().name).isEqualTo(savedUser.name)
        assertThat(result.get().age).isEqualTo(savedUser.age)
        assertThat(result.get().id).isEqualTo(savedUser.id)
    }
}