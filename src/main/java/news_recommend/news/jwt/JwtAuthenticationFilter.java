package news_recommend.news.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("🛡️ JwtAuthenticationFilter: 요청 들어옴");

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제거
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailFromToken(token);
                System.out.println("✅ 토큰 유효함, 사용자: " + email);

                JwtAuthenticationToken authentication = new JwtAuthenticationToken(email, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("🔐 인증 객체 설정 완료: " + authentication);
            } else {
                System.out.println("❌ 토큰 유효하지 않음");
            }
        } else {
            System.out.println("❌ Authorization 헤더 없음 or 잘못됨");
        }

        filterChain.doFilter(request, response);
    }
}