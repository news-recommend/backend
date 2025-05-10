package news_recommend.news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public SecurityConfig() {
        System.out.println("✅ SecurityConfig Loaded!");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (개발용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/signup").permitAll() // 회원가입은 인증 없이 허용
                        .anyRequest().authenticated() // 그 외는 인증 필요
                )
                .httpBasic(Customizer.withDefaults()); // Basic 인증 (필요 시 로그인용)
        return http.build();
    }
}