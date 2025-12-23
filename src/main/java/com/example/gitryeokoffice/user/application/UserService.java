package com.example.gitryeokoffice.user.application;

import com.example.gitryeokoffice.global.exception.ApplicationException;
import com.example.gitryeokoffice.global.util.JwtTokenProvider;
import com.example.gitryeokoffice.user.domain.User;
import com.example.gitryeokoffice.user.domain.UserRepository;
import com.example.gitryeokoffice.user.exception.UserException;
import com.example.gitryeokoffice.user.presentation.dto.LoginRequest;
import com.example.gitryeokoffice.user.presentation.dto.SignupRequest;
import com.example.gitryeokoffice.user.presentation.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User 도메인 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public UserResponse signup(SignupRequest request) {
        // 닉네임 중복 체크
        if (userRepository.existsByNickname(request.nickname())) {
            throw new ApplicationException(UserException.DUPLICATE_NICKNAME);
        }

        // GitHub ID 중복 체크
        if (userRepository.existsByGithubId(request.githubId())) {
            throw new ApplicationException(UserException.DUPLICATE_GITHUB_ID);
        }

        // User 엔티티 생성 및 저장
        User user = User.create(
                request.password(), // TODO: 비밀번호 암호화 필요 (BCrypt 등)
                request.nickname(),
                request.githubId(),
                request.jobType()
        );

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료. userId: {}, nickname: {}", savedUser.getId(), savedUser.getNickname());

        return UserResponse.from(savedUser);
    }

    /**
     * 로그인
     */
    public String login(LoginRequest request) {
        // 닉네임으로 사용자 조회
        User user = userRepository.findByNickname(request.nickname())
                .orElseThrow(() -> new ApplicationException(UserException.USER_NOT_FOUND));

        // 비밀번호 검증 (TODO: BCrypt 사용)
        if (!user.getPassword().equals(request.password())) {
            throw new ApplicationException(UserException.INVALID_PASSWORD);
        }

        // JWT 토큰 생성 및 반환
        String token = jwtTokenProvider.createToken(user.getId());
        log.info("로그인 성공. userId: {}, nickname: {}", user.getId(), user.getNickname());

        return token;
    }

    /**
     * 사용자 정보 조회
     */
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserException.USER_NOT_FOUND));

        return UserResponse.from(user);
    }
}
