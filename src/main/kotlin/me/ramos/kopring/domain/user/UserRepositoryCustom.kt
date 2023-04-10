package me.ramos.kopring.domain.user

interface UserRepositoryCustom {

    fun findAllWithHistories(): List<User>
}