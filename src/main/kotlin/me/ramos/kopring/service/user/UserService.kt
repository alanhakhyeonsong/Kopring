package me.ramos.kopring.service.user

import me.ramos.kopring.domain.user.User
import me.ramos.kopring.domain.user.UserRepository
import me.ramos.kopring.dto.user.request.UserCreateRequest
import me.ramos.kopring.dto.user.request.UserUpdateRequest
import me.ramos.kopring.dto.user.response.UserResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun saveUser(request: UserCreateRequest) {
        userRepository.save(User(request.name, request.age))
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        return userRepository.findAll()
            .map(UserResponse::of)
    }

    @Transactional
    fun updateUsername(request: UserUpdateRequest) {
        val user =
            userRepository.findByIdOrNull(request.id) ?: throw IllegalArgumentException()

        user.updateName(request.name)
    }

    @Transactional
    fun deleteUser(name: String) {
        val user = userRepository.findByName(name) ?: throw IllegalArgumentException()
        userRepository.delete(user)
    }
}