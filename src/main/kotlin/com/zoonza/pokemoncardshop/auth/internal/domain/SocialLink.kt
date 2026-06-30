package com.zoonza.pokemoncardshop.auth.internal.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_social_link_provider_social_id",
            columnNames = ["provider", "social_id"]
        ),
        UniqueConstraint(
            name = "uk_social_link_member_id_provider",
            columnNames = ["member_id", "provider"]
        )
    ]
)
class SocialLink private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false)
    val memberId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val provider: OAuth2Provider,

    @Column(nullable = false, length = 255)
    val socialId: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = true)
    var lastLoginAt: LocalDateTime? = null,
) {
    fun login(loggedInAt: LocalDateTime) {
        this.lastLoginAt = loggedInAt
    }

    companion object {
        fun register(
            memberId: Long,
            provider: OAuth2Provider,
            socialId: String,
            createdAt: LocalDateTime,
        ): SocialLink {
            return SocialLink(
                memberId = memberId,
                provider = provider,
                socialId = socialId,
                createdAt = createdAt,
            )
        }
    }
}