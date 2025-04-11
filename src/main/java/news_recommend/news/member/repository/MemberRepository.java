package news_recommend.news.member.repository;

import news_recommend.news.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(final Member account);

    Optional<Member> findById(final Long id);

    List<Member> findAll();

    int update(final Member account);

    int delete(final Long id);

}
