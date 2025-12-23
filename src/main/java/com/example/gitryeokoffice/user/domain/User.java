package com.example.gitryeokoffice.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 엔티티
 * GitHub 활동 로그 기반 AI 성향 분석을 위한 사용자 정보 관리
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String githubId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Column(columnDefinition = "TEXT")
    private String vibeStatus; // AI 분석 결과 (JSON 혹은 String)

    // 정적 팩토리 메서드
    public static User create(String password, String nickname, String githubId, JobType jobType) {
        User user = new User();
        user.password = password;
        user.nickname = nickname;
        user.githubId = githubId;
        user.jobType = jobType;
        return user;
    }

    // vibeStatus 업데이트 메서드 (AI 분석 결과 저장)
    public void updateVibeStatus(String vibeStatus) {
        this.vibeStatus = vibeStatus;
    }
}
