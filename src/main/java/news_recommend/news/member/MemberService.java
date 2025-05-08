package news_recommend.news.member;

import news_recommend.news.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;
    public MemberService(final MemberRepository accountRepository){
        this.memberRepository = accountRepository;
    }


//
//    public List<Member> findMembers() {
//        return memberRepository.findAll();
//    }
//
//    public Member save(Member member) {
//        return memberRepository.save(member);
//    }
//
//    public Optional<Member> findById(Long id) {
//        return memberRepository.findById(id);
//    }
//
//    public int update(Member member) {
//        return memberRepository.update(member);
//    }
//
//    public int delete(Long id) {
//        return memberRepository.delete(id);
//    }

    // 이 위에거는 참고용으로만 그리고 member라는거 변경하는게 좋은가

    // 내 프로필 조회
    // 추후에 jwt 토큰 사용한걸로 변경 필요
    public Member findMyProfile() {
        return memberRepository.findMyProfile();
    }

    // 내 프로필 수정
    public void updateMyProfile(MemberUpdateRequest request) {
        // userId는 인증에서 가져온다고 가정
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
            String currentPassword = memberRepository.findPasswordByUserId(userId); // 예시
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



}
