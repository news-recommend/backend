package news_recommend.news.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;



@Component
public class JwtTokenProvider {

    private final String secret = "my-super-secret-key-for-jwt-signing-1234567890"; // 최소 32자 이상
    private final Key secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    private final long validityInMilliseconds = 3600000; // 1시간

    public String createAccessToken(Long userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000 * 60 * 60); // 1시간

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }
    public String createRefreshToken(Long userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000L * 60 * 60 * 24 * 7); // 7일

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println(" 토큰 유효성 검사 시작");
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            System.out.println("✅ 토큰 유효성 통과");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ 토큰 유효성 실패: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    @PostConstruct
    public void logKey() {
        System.out.println("🔐 JWT SecretKey Hash: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }
}
