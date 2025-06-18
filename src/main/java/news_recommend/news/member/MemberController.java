package news_recommend.news.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import news_recommend.news.jwt.JwtTokenProvider;
import news_recommend.news.member.dto.LoginRequest;
import news_recommend.news.member.dto.SignupRequest;
import news_recommend.news.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")  // 경로 통일(충돌 해결)

public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;


    public MemberController(final MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //  회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> signup(@RequestBody SignupRequest request) {
        try {
            if (request.getPreferredTags() == null) {
                request.setPreferredTags(List.of());
            }
            System.out.println("ageGroup = " + request.getAgeGroup());
            System.out.println("gender = " + request.getGender());
            memberService.signup(request);

            Map<String, Object> result = new HashMap<>();
            result.put("registered", true);

            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(e.getMessage(), "validation")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("알 수 없는 오류가 발생했습니다.", "server_error"));
        }
    }

    // 내 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Member>> getMyProfile() {
        try {
            Member member = memberService.findMyProfile();
            return ResponseEntity.ok(ApiResponse.success(member));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("프로필을 찾을 수 없습니다.", "not_found"));
        }
    }

    //  내 프로필 수정
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, String>>> updateMyProfile(@RequestBody MemberUpdateRequest request) {
        try {
            memberService.updateMyProfile(request);
            Map<String, String> result = new HashMap<>();
            result.put("message", "변경 성공");
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), "password"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력 형식이 올바르지 않습니다.", "type"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
        try {
            Map<String, String> tokens = memberService.loginWithTokens(request.getEmail(), request.getPassword());
            String accessToken = (String) tokens.get("accessToken");
            String refreshToken = (String) tokens.get("refreshToken");

            // HttpOnly 쿠키 설정
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .domain("newrecommend.shop")
                    .build();

            // userId, accessToke 정보를 응답에 포함
            Map<String, Object> result = new HashMap<>();
            result.put("userId", tokens.get("userId"));
            result.put("accessToken", accessToken);
            System.out.println("result: " +ApiResponse.success(result));
            return ResponseEntity.ok()
                    .header("Set-Cookie", cookie.toString())
                    .body(ApiResponse.success(result));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), "login_error"));
        }
    }

    // 로그인 유지
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<Map<String, Object>>> reissueAccessToken(HttpServletRequest request) {
        String refreshToken = extractCookie(request, "refreshToken");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("유효하지 않은 리프레시 토큰입니다.", "unauthorized"));
        }

        String userEmail = jwtTokenProvider.getEmailFromToken(refreshToken);
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userId, userEmail);  // ✅ 이메일과 ID 모두 전달

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", newAccessToken);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 로그아웃 (쿠키삭제)
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        // refreshToken 쿠키 삭제
        Cookie expiredCookie = new Cookie("refreshToken", null);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0); // 즉시 만료
        expiredCookie.setHttpOnly(true);
        expiredCookie.setSecure(false); // HTTPS 사용시 true 권장

        response.addCookie(expiredCookie);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}