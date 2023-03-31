package me.ramos.kopring.dto.user.response

import me.ramos.kopring.domain.user.User

data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int?
) {

    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                age = user.age
            )
        }
    }
}