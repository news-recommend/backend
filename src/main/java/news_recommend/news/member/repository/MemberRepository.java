package news_recommend.news.member.repository;

import news_recommend.news.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(final Member member);

    Optional<Member> findById(final Long id);

    List<Member> findAll();

    int update(final Member member);

    int delete(final Long id);

    Member findMyProfile();

    String findPasswordByUserId(Long userId);
    void updatePassword(Long userId, String newPassword);

}
