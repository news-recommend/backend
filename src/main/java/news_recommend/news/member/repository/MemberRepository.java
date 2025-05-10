package news_recommend.news.member.repository;

import news_recommend.news.member.Member;
//import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
//import news_recommend.news.member.Member;


public interface MemberRepository {
    Member save(final Member member);

    Optional<Member> findById(final Long id);

    List<Member> findAll();

    int update(final Member member);

    int delete(final Long id);

    Member findMyProfile();

    String findPasswordByUserId(Long userId);
    void updatePassword(Long userId, String newPassword);

    // 회원가입을 위한 이메일 확인 기능
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}

