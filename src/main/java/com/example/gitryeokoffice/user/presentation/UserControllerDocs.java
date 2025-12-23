package com.example.gitryeokoffice.user.presentation;

import com.example.gitryeokoffice.global.auth.LoginUser;
import com.example.gitryeokoffice.global.exception.ErrorResponse;
import com.example.gitryeokoffice.user.presentation.dto.LoginRequest;
import com.example.gitryeokoffice.user.presentation.dto.SignupRequest;
import com.example.gitryeokoffice.user.presentation.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * User API Swagger 문서
 */
@Tag(name = "User API", description = "사용자 인증 및 관리 API")
public interface UserControllerDocs {

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다. 닉네임과 GitHub ID는 중복될 수 없습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "회원가입 성공 예시",
                                    value = """
                                            {
                                              "id": 1,
                                              "nickname": "개발자혜림",
                                              "githubId": "hyerim123",
                                              "jobType": "DEVELOPER",
                                              "vibeStatus": null
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "입력값 검증 실패",
                                            value = """
                                                    {
                                                      "title": "유효하지 않은 입력값",
                                                      "status": 400,
                                                      "detail": "nickname: 닉네임은 필수입니다., password: 비밀번호는 필수입니다.",
                                                      "instance": "/api/users/signup"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),

            @ApiResponse(responseCode = "409", description = "중복된 데이터",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "닉네임 중복",
                                            value = """
                                                    {
                                                      "title": "닉네임 중복",
                                                      "status": 409,
                                                      "detail": "이미 사용 중인 닉네임입니다.",
                                                      "instance": "/api/users/signup"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "GitHub ID 중복",
                                            value = """
                                                    {
                                                      "title": "GitHub ID 중복",
                                                      "status": 409,
                                                      "detail": "이미 등록된 GitHub ID입니다.",
                                                      "instance": "/api/users/signup"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),

            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "title": "알 수 없는 오류",
                                              "status": 500,
                                              "detail": "서버 내부에 알 수 없는 오류가 발생했습니다. 관리자에게 문의 하세요.",
                                              "instance": "/api/users/signup"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request);

    @Operation(
            summary = "로그인",
            description = "닉네임과 비밀번호로 로그인하고 JWT 토큰을 쿠키로 발급받습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "로그인 성공 (쿠키 발급)",
                                    value = "쿠키에 accessToken 발급"
                            )
                    )
            ),

            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "입력값 검증 실패",
                                    value = """
                                            {
                                              "title": "유효하지 않은 입력값",
                                              "status": 400,
                                              "detail": "nickname: 닉네임은 필수입니다.",
                                              "instance": "/api/users/login"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "사용자를 찾을 수 없음",
                                            value = """
                                                    {
                                                      "title": "사용자를 찾을 수 없음",
                                                      "status": 404,
                                                      "detail": "해당 사용자를 찾을 수 없습니다.",
                                                      "instance": "/api/users/login"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "비밀번호 불일치",
                                            value = """
                                                    {
                                                      "title": "비밀번호 불일치",
                                                      "status": 401,
                                                      "detail": "비밀번호가 일치하지 않습니다.",
                                                      "instance": "/api/users/login"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),

            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "title": "알 수 없는 오류",
                                              "status": 500,
                                              "detail": "서버 내부에 알 수 없는 오류가 발생했습니다. 관리자에게 문의 하세요.",
                                              "instance": "/api/users/login"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response);

    @Operation(
            summary = "로그아웃",
            description = "쿠키에서 JWT 토큰을 제거합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "로그아웃 성공",
                                    value = "쿠키에서 accessToken 제거"
                            )
                    )
            )
    })
    ResponseEntity<Void> logout(HttpServletResponse response);

    @Operation(
            summary = "현재 로그인한 사용자 정보 조회",
            description = "JWT 토큰으로 인증된 현재 사용자의 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 정보 조회 성공",
                                    value = """
                                            {
                                              "id": 1,
                                              "nickname": "개발자혜림",
                                              "githubId": "hyerim123",
                                              "jobType": "DEVELOPER",
                                              "vibeStatus": "{\\"planType\\":\\"계획형\\",\\"focusType\\":\\"몰입형\\",\\"timeType\\":\\"아침형\\"}"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "토큰 누락",
                                            value = """
                                                    {
                                                      "title": "토큰 누락",
                                                      "status": 401,
                                                      "detail": "인증 토큰이 없습니다.",
                                                      "instance": "/api/users/me"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "유효하지 않은 토큰",
                                            value = """
                                                    {
                                                      "title": "유효하지 않은 토큰",
                                                      "status": 401,
                                                      "detail": "유효하지 않거나 만료된 토큰입니다.",
                                                      "instance": "/api/users/me"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),

            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자를 찾을 수 없음",
                                    value = """
                                            {
                                              "title": "사용자를 찾을 수 없음",
                                              "status": 404,
                                              "detail": "해당 사용자를 찾을 수 없습니다.",
                                              "instance": "/api/users/me"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "title": "알 수 없는 오류",
                                              "status": 500,
                                              "detail": "서버 내부에 알 수 없는 오류가 발생했습니다. 관리자에게 문의 하세요.",
                                              "instance": "/api/users/me"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<UserResponse> getCurrentUser(@LoginUser Long userId);
}
