package news_recommend.news.member;

import news_recommend.news.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;
    public MemberService(final MemberRepository accountRepository){
        this.memberRepository = accountRepository;
    }



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


}
