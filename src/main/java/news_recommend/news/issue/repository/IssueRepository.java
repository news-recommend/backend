package news_recommend.news.issue.repository;

import news_recommend.news.issue.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    Issue save(final Issue account);

    Optional<Issue> findById(final Long id);

    List<Issue> findAll();

    int update(final Issue account);

    int delete(final Long id);

}
