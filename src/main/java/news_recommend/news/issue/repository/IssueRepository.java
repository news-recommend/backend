package news_recommend.news.issue.repository;

import news_recommend.news.issue.Issue;
import news_recommend.news.issue.dto.IssuePreviewResponse;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    Issue save(final Issue account);

    Optional<Issue> findById(final Long id);

    List<Issue> findAll();

    int update(final Issue account);

    int delete(final Long id);

    // 카테고리별 이슈 리스트 코드 추가
    List<Issue> findByCategory(String category, int limit, int offset);

    int countByCategory(String category);
    // 카테고리별 이슈 리스트 코드 추가 끝

    Optional<Issue> findByName(String name);

    int countByKeyword(String keyword);

    List<IssuePreviewResponse> findByKeywordAndSort(String keyword, String sort, int limit, int offset);
}