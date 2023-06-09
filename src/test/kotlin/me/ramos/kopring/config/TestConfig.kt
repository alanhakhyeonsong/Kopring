package me.ramos.kopring.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@TestConfiguration
class TestConfig(
    @PersistenceContext private val em: EntityManager
) {

    @Bean
    fun queryFactory(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}