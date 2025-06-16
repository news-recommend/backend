package news_recommend.news.issue.repository.jdbctemplate;

import lombok.RequiredArgsConstructor;
import news_recommend.news.issue.dto.RawNews;
import news_recommend.news.issue.repository.NewsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcTemplateNewsRepository implements NewsRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(RawNews news, Long issueId) {
        String sql = "INSERT INTO news (issue_id, title, link, description, pub_date, sentiment_score) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                issueId,
                news.getTitle(),
                news.getLink(),
                news.getDescription(),
                news.getPubDate(),
                news.getSentimentScore());
    }

    @Override
    public List<RawNews> findByIssueId(Long issueId) {
        String sql = "SELECT * FROM news WHERE issue_id = ?";
        return jdbcTemplate.query(sql, newsRowMapper(), issueId);
    }

    private RowMapper<RawNews> newsRowMapper() {
        return (rs, rowNum) -> {
            RawNews news = new RawNews();
            news.setTitle(rs.getString("title"));
            news.setLink(rs.getString("link"));
            news.setDescription(rs.getString("description"));
            news.setPubDate(rs.getString("pub_date"));
            news.setSentimentScore((Integer) rs.getObject("sentiment_score"));  // 감정 점수는 nullable
            return news;
        };
    }
}
