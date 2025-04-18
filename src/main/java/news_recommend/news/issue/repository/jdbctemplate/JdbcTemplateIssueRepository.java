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
        String sql = "INSERT INTO issue (issue_name, category, emotion) VALUES (?, ?, ?)";
        Long issueId = jdbcTemplate.queryForObject(sql, new Object[]{
                issue.getIssueName(),
                issue.getCategory(),
                issue.getIssueName()
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




    private RowMapper<Issue> rowMapper() {
        return (rs, rowNum ) -> new Issue(
                rs.getLong("issue_id"),
                rs.getString("issue_name"),
                rs.getString("category"),
                rs.getString("emotion")
        );
    }
}