package news_recommend.news.auth;

import lombok.RequiredArgsConstructor;
import news_recommend.news.jwt.JwtTokenProvider;
import news_recommend.news.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    // accessToken 유효성 검사
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken() {
        return ResponseEntity.ok(ApiResponse.success("유효한 토큰입니다."));
    }

    // refreshToken을 이용해 accessToken 재발급
    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshAccessToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("유효하지 않은 리프레시 토큰입니다.", "unauthorized"));
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userId, email);

        Map<String, String> result = new HashMap<>();
        result.put("accessToken", newAccessToken);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}