package news_recommend.news.issue.repository.jdbctemplate;


import news_recommend.news.issue.Issue;
import news_recommend.news.issue.dto.IssuePreviewResponse;
import news_recommend.news.issue.repository.IssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class JdbcTemplateIssueRepository implements IssueRepository {

    public static final Logger log =  LoggerFactory.getLogger(JdbcTemplateIssueRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateIssueRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }



    @Override
    public List<Issue> findAll() {
        String sql =  "SELECT * FROM issue";
        List<Issue> issueList = jdbcTemplate.query(sql, rowMapper());
        log.debug("issueList: {}", issueList.toString());
        return issueList;
    }

    @Override
    public Issue save(Issue issue) {
        String sql = "INSERT INTO issue (issue_name, category, emotion, news_list) VALUES (?, ?, ?, ?) RETURNING issue_id";
        System.out.println(issue);
        Long issueId = jdbcTemplate.queryForObject(sql, new Object[]{
                issue.getIssueName(),
                issue.getCategory(),
                issue.getEmotion(),
                issue.getNewsList()
        }, Long.class);

        issue.setIssueId(issueId);
        return issue;
    }

    @Override
    public Optional<Issue> findById(Long id) {
        String sql = "SELECT * FROM issue WHERE issue_id = ?";
        List<Issue> result = jdbcTemplate.query(sql, rowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public int update(Issue issue) {
        String sql = "UPDATE issue SET issue_name = ?, category = ?, emotion = ? WHERE issue_id = ?";
        return jdbcTemplate.update(sql,
                issue.getIssueName(),
                issue.getCategory(),
                issue.getEmotion(),
                issue.getIssueId()
        );
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM issue WHERE issue_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // 카테고리별 이슈 리스트 코드 추가
    @Override
    public List<Issue> findByCategory(String category, int limit, int offset) {
        String sql = "SELECT * FROM issue WHERE category = ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper(), category, limit, offset);
    }

    @Override
    public int countByCategory(String category) {
        String sql = "SELECT COUNT(*) FROM issue WHERE category = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, category);
    }
    // 카테고리별 이슈 리스트 코드 추가 끝

    @Override
    public Optional<Issue> findByName(String name) {
        String sql = "SELECT * FROM issue WHERE issue_name = ? LIMIT 1";
        List<Issue> result = jdbcTemplate.query(sql, rowMapper(), name);
        return result.stream().findAny();
    }


    private RowMapper<Issue> rowMapper() {
        return (rs, rowNum) -> {
            Issue issue = new Issue();
            issue.setIssueId(rs.getLong("issue_id"));
            issue.setIssueName(rs.getString("issue_name"));
            issue.setCategory(rs.getString("category"));
            issue.setEmotion(rs.getString("emotion"));
            issue.setNewsList(rs.getString("news_list"));
            issue.setThumbnail(rs.getString("thumbnail"));
            issue.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return issue;
        };
    }

    public List<IssuePreviewResponse> findByKeywordAndSort(String keyword, String sort, int limit, int offset) {
        boolean isPopular = "popular".equals(sort);

        String orderBy = isPopular
                ? "bookmark_count DESC"
                : "i.created_at DESC";

        String sql = """
        SELECT i.issue_id, i.issue_name, i.category, i.news_list
        """ + (isPopular ? ", COUNT(im.issue_id) AS bookmark_count " : "") + """
        FROM issue i
        """ + (isPopular ? "LEFT JOIN issue_user im ON i.issue_id = im.issue_id " : "") + """
        WHERE LOWER(i.issue_name) LIKE ?
        """ + (isPopular ? "GROUP BY i.issue_id, i.issue_name, i.category, i.news_list " : "") + """
        ORDER BY """ + " " + orderBy + """
        LIMIT ? OFFSET ?
    """;

        System.out.println("SQL: " + sql);
        System.out.println("Keyword: %" + keyword + "%");
        System.out.println("Limit: " + limit + ", Offset: " + offset);

        return jdbcTemplate.query(sql, (rs, rowNum) -> new IssuePreviewResponse(
                rs.getLong("issue_id"),
                rs.getString("issue_name"),
                rs.getString("category"),
                new ArrayList<>(),     // sentimentTrend
                null,                  // thumbnail
                false,                 // isBookmarked
                new ArrayList<>()      // newsList (빈 리스트 전달)
        ), "%" + keyword.toLowerCase() + "%", limit, offset);
    }


    public int countByKeyword(String keyword) {
        String sql = "SELECT COUNT(*) FROM issue WHERE issue_name LIKE ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, "%" + keyword + "%");
    }

    private List<String> parseNewsList(String rawNewsList) {
        if (rawNewsList == null || rawNewsList.isBlank()) return List.of();
        return Arrays.stream(rawNewsList.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}