package com.example.gitryeokoffice.user.presentation.dto;

import com.example.gitryeokoffice.user.domain.JobType;
import com.example.gitryeokoffice.user.domain.User;

/**
 * 사용자 정보 응답 DTO
 */
public record UserResponse(
        Long id,
        String nickname,
        String githubId,
        JobType jobType,
        String vibeStatus
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getGithubId(),
                user.getJobType(),
                user.getVibeStatus()
        );
    }
}
