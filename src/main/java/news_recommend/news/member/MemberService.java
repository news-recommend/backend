package news_recommend.news.member;

import news_recommend.news.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;
    public MemberService(final MemberRepository accountRepository){
        this.memberRepository = accountRepository;
    }



    List<Member> findMembers() {
        return memberRepository.findAll();
    }


}
