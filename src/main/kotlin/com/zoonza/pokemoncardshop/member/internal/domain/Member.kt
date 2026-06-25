package com.zoonza.pokemoncardshop.member.internal.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Member private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "nickname", nullable = false, unique = true, length = 12))
    var nickname: Nickname,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false)
    var updatedAt: LocalDateTime,
) {
    companion object {
        fun register(nickname: Nickname, createdAt: LocalDateTime): Member {
            return Member(
                nickname = nickname,
                createdAt = createdAt,
                updatedAt = createdAt
            )
        }
    }
}