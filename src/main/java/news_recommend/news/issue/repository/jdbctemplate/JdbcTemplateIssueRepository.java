package news_recommend.news.issue.repository.jdbctemplate;


import news_recommend.news.issue.Issue;
import news_recommend.news.issue.repository.IssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
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
}