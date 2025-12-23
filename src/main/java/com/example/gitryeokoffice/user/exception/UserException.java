package com.example.gitryeokoffice.user.exception;

import com.example.gitryeokoffice.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * User 도메인 관련 예외 코드
 */
@AllArgsConstructor
public enum UserException implements ExceptionCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없음", "해당 사용자를 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "닉네임 중복", "이미 사용 중인 닉네임입니다."),
    DUPLICATE_GITHUB_ID(HttpStatus.CONFLICT, "GitHub ID 중복", "이미 등록된 GitHub ID입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호 불일치", "비밀번호가 일치하지 않습니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 누락", "인증 토큰이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰", "유효하지 않거나 만료된 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String detail;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
