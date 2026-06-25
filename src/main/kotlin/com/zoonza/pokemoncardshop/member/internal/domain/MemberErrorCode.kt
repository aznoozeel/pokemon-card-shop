package com.zoonza.pokemoncardshop.member.internal.domain

import com.zoonza.pokemoncardshop.common.error.ErrorCode
import com.zoonza.pokemoncardshop.common.error.ErrorStatus

enum class MemberErrorCode(
    override val status: ErrorStatus,
    override val message: String
) : ErrorCode {
    NICKNAME_LENGTH_OUT_OF_RANGE(ErrorStatus.BAD_REQUEST, "닉네임은 3자 이상 12자 이하여야 합니다."),
    NICKNAME_CONTAINS_WHITESPACE(ErrorStatus.BAD_REQUEST, "닉네임에는 공백을 사용할 수 없습니다."),
    NICKNAME_GENERATION_FAILED(ErrorStatus.INTERNAL_SERVER_ERROR, "사용 가능한 닉네임을 생성하지 못했습니다."),
    NOT_FOUND(ErrorStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
    ;

    override val code: String = "MEMBER_$name"
}
