package com.example.gitryeokoffice.user.presentation;

import com.example.gitryeokoffice.global.auth.LoginUser;
import com.example.gitryeokoffice.global.util.CookieUtil;
import com.example.gitryeokoffice.user.application.UserService;
import com.example.gitryeokoffice.user.presentation.dto.LoginRequest;
import com.example.gitryeokoffice.user.presentation.dto.SignupRequest;
import com.example.gitryeokoffice.user.presentation.dto.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User 관련 API 엔드포인트
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        String token = userService.login(request);
        CookieUtil.addTokenCookie(response, token);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtil.removeTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@LoginUser Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }
}
