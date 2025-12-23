package com.example.gitryeokoffice.user.presentation.dto;

import com.example.gitryeokoffice.user.domain.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 회원가입 요청 DTO
 */
public record SignupRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "GitHub ID는 필수입니다.")
        String githubId,

        @NotNull(message = "직무 타입은 필수입니다.")
        JobType jobType
) {
}
