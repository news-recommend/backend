package news_recommend.news.issue.repository;

import news_recommend.news.issue.dto.RawNews;
import java.util.List;

public interface NewsRepository {
    void save(RawNews news, Long issueId);
    List<RawNews> findByIssueId(Long issueId);
}
