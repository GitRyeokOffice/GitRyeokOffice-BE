1. 프로젝트 개요
   깃력사무소 (GitRyeokOffice): GitHub 활동 로그 기반 AI 성향 분석 팀빌딩 플랫폼. 사용자의 GitHub 데이터를 1차 가공하여 OpenAI API로 전달하고, AI가 분석한 'Dev-Vibe' 카드(성향 분석) 및 매칭 사유를 제공함.

2. 기술 스택
   Back-end: Java 21, Spring Boot 3.5.x

Data: Spring Data JPA, MySQL 8.0

AI: OpenAI API (GPT-4o)

External API: GitHub REST API (Octokit 스타일 또는 WebClient 사용)

3. 핵심 비즈니스 로직 (Dev-Vibe 3축)
   계획형 vs 즉흥형: 이슈 생성 빈도 및 PR 연동률 기반

지속형 vs 몰입형: 잔디 밀도 및 연속 커밋 일수 기반

아침형 vs 저녁형: 커밋 타임스탬프 기반 (06:00 기준)

4. 코딩 가이드
   계층 구조: controller -> service -> repository (단순화된 3계층)

DTO: record 사용 필수

네이밍: 축약어를 지양하고 의도가 명확한 한글 주석을 필요한 곳에 추가

Exception: 도메인별 복잡한 Enum 대신 RuntimeException 기반의 공통 예외 처리 혹은 단순한 ErrorResponse 반환

Lombok: @Getter, @RequiredArgsConstructor 등 적극 활용

5. Git 규칙 (해커톤 간소화)
   브랜치 전략: main 브랜치 기반, 작업 시 개인별 브랜치 사용 (예: hyerimh/feat-1)

커밋 메시지: Conventional Commits 준수 (한글 작성)

형식: feat: 기능 설명, fix: 버그 수정 등

주의 사항:

AI 도구 흔적 금지: 커밋 메시지에 🤖 Generated with [Claude Code] 또는 Co-Authored-By: Claude 등 Claude 관련 문구를 절대 포함하지 말 것.

모든 커밋은 사람이 직접 작성한 것처럼 자연스러워야 함.

6. 개발 필수 체크리스트
   보안: GitHub Token, OpenAI API Key는 반드시 .env 또는 application-secret.yml로 관리 (git 추적 금지)

AI 연동: GitHub 데이터를 문자열/JSON으로 요약하여 OpenAI에 전송할 때 토큰 효율성을 고려할 것

로직: username 하나만으로 GitHub 공개 이벤트를 긁어오는 로직이 핵심

7. 실행 명령어
   빌드/실행: ./gradlew bootRun

테스트: ./gradlew test

필드명,타입,설명
id,Long,Primary Key (Auto Increment)
password,String,암호화된 비밀번호
nickname,String,서비스 내 닉네임
githubId,String,GitHub 연동용 아이디 (분석의 시작점)
jobType,Enum,"DEVELOPER, DESIGNER, PLANNER (설문 기반)"
vibeStatus,String,AI 분석 결과 요약 (JSON 혹은 String)
이걸기반으로 로그인 기능을 구현할거야


package com.ebbinghaus.ttopullae.global.auth;

import com.ebbinghaus.ttopullae.global.exception.ApplicationException;
import com.ebbinghaus.ttopullae.global.util.CookieUtil;
import com.ebbinghaus.ttopullae.global.util.JwtTokenProvider;
import com.ebbinghaus.ttopullae.user.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
* JWT 토큰 인증을 처리하는 인터셉터
* 쿠키에서 토큰을 추출하고 검증한 후, 사용자 ID를 request attribute에 저장
  */
  @Slf4j
  @Component
  @RequiredArgsConstructor
  public class JwtAuthenticationInterceptor implements HandlerInterceptor {

  private final JwtTokenProvider jwtTokenProvider;
  private static final String USER_ID_ATTRIBUTE = "userId";

  @Override
  public boolean preHandle(
  HttpServletRequest request,
  HttpServletResponse response,
  Object handler
  ) {

       if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
           return true;
       }

       // 쿠키에서 JWT 토큰 추출
       String token = CookieUtil.extractToken(request.getCookies());

       // 토큰 유효성 검증 (예외 발생 시 GlobalExceptionHandler에서 처리)
       jwtTokenProvider.validateToken(token);

       // 토큰에서 사용자 ID 추출 후 request attribute에 저장
       Long userId = jwtTokenProvider.getUserId(token);
       request.setAttribute(USER_ID_ATTRIBUTE, userId);

       log.debug("사용자 인증 성공. userId: {}, URI: {}", userId, request.getRequestURI());
       return true;
  }
  }


package com.ebbinghaus.ttopullae.global.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 컨트롤러 메서드 파라미터에 현재 로그인한 사용자의 ID를 주입하기 위한 어노테이션
*
* 사용 예시:
* public ResponseEntity<?> createStudyRoom(@LoginUser Long userId, @RequestBody StudyRoomRequest request)
  */
  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface LoginUser {
  }


package com.ebbinghaus.ttopullae.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
* @LoginUser 어노테이션이 붙은 파라미터에 현재 로그인한 사용자 ID를 주입하는 ArgumentResolver
  */
  @Component
  public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String USER_ID_ATTRIBUTE = "userId";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
  // @LoginUser 어노테이션이 있고, Long 타입인 파라미터만 지원
  return parameter.hasParameterAnnotation(LoginUser.class)
  && Long.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
  MethodParameter parameter,
  ModelAndViewContainer mavContainer,
  NativeWebRequest webRequest,
  WebDataBinderFactory binderFactory
  ) {
  // Interceptor에서 request attribute에 저장한 userId를 가져옴
  HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
  return request.getAttribute(USER_ID_ATTRIBUTE);
  }
  }
* 이런식으로 구성을 할건데 이 코드들은 다른 플젝 코드를 가져온거라서 그대로 하면 안되고 넌 user도메인 안에서 DDD구조를 가지고 앞으로 코드를 짤거야
  application
  domain
  exception
  presentation을 둘거고 아까 완성한 exception도 해주면 돼 자 내가 준 도메인을 기준으로 작업을 시작해줘
