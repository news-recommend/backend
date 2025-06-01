package news_recommend.news.member;

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

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    //  회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> signup(@RequestBody SignupRequest request) {
        try {
            if (request.getPreferredTags() == null) {
                request.setPreferredTags(List.of());
            }

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
    @GetMapping("/me")
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
                    .secure(false)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            // userId, accessToke 정보를 응답에 포함
            Map<String, Object> result = new HashMap<>();
            result.put("userId", tokens.get("userId"));
            result.put("accessToken", accessToken);

            return ResponseEntity.ok()
                    .header("Set-Cookie", cookie.toString())
                    .body(ApiResponse.success(result));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), "login_error"));
        }
    }
}