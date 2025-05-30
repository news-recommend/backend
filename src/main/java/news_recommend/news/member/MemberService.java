package news_recommend.news.member;
import news_recommend.news.member.dto.SignupRequest;
import news_recommend.news.member.repository.MemberRepository;
import news_recommend.news.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;



    public MemberService(final MemberRepository accountRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = accountRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public int update(Member member) {
        return memberRepository.update(member);
    }

    public int delete(Long id) {
        return memberRepository.delete(id);
    }

    // 이 위에거는 참고용으로만 그리고 member라는거 변경하는게 좋은가

    // 내 프로필 조회
    // 추후에 jwt 토큰 사용한걸로 변경 필요
    public Member findMyProfile() {
        return memberRepository.findMyProfile();
    }

    public void updateMyProfile(MemberUpdateRequest request) {
        Long userId = 1L; // TODO: JWT에서 추출하도록 변경

        Optional<Member> memberOpt = memberRepository.findById(userId);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        Member member = memberOpt.get();
        boolean changed = false;

        if (request.getName() != null) {
            member.setName(request.getName());
            changed = true;
        }
        if (request.getEmail() != null) {
            member.setEmail(request.getEmail());
            changed = true;
        }
        if (request.getInterestCategory() != null) {
            member.setInterestCategory(request.getInterestCategory());
            changed = true;
        }
        if (request.getNowPassword() != null && request.getNewPassword() != null) {
            // 실제로는 비밀번호는 별도 테이블/필드에 암호화되어 있어야 함
            String currentPassword = memberRepository.findPasswordByUserId(userId);
            // 예시
            if (!request.getNowPassword().equals(currentPassword)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            memberRepository.updatePassword(userId, request.getNewPassword());
            changed = true;
        } else if (request.getNowPassword() != null || request.getNewPassword() != null) {
            throw new IllegalArgumentException("입력 형식이 올바르지 않습니다.");
        }

        if (changed) {
            memberRepository.update(member);
        }
    }

    // 이메일로 회원조회
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // 이메일 중복 검사
    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    // 회원가입 기능
    public void signup(SignupRequest request) {
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        List<String> tags = request.getPreferredTags();
        String joinedTags = (tags == null || tags.isEmpty())
                ? ""
                : String.join(",", tags.stream().filter(tag -> tag != null).toList());

        Member member = new Member(
                request.getName(),
                request.getEmail(),
                joinedTags,
                request.getPassword()
        );

        save(member);
    }
    // 로그인: 사용자가 이메일과 비밀번호로 로그인 요청을 보냄
    // accessToken + refreshToken을 반환
    public Map<String, String> loginWithTokens(String email, String password) {
        Optional<Member> optional = memberRepository.findByEmail(email);

        if (optional.isEmpty()) {
            System.out.println("❌ 로그인 실패: 이메일 존재하지 않음");
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        Member member = optional.get();

        //  여기서 확인 로그 출력
        System.out.println("🔐 로그인 시도");
        System.out.println("입력한 이메일: " + email);
        System.out.println("입력한 비밀번호: " + password);
        System.out.println("DB 저장 비밀번호: " + member.getPassword());

        // 실제 서비스에서는 암호화된 비밀번호 비교 필요
        if (!password.equals(member.getPassword())) {
            System.out.println("❌ 로그인 실패: 비밀번호 불일치");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getUserId(), member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUserId(), member.getEmail());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }


}