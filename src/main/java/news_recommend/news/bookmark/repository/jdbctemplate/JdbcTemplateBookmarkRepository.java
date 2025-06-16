package news_recommend.news.bookmark.repository.jdbctemplate;


import lombok.RequiredArgsConstructor;
import news_recommend.news.bookmark.repository.BookmarkRepository;
import news_recommend.news.bookmark.Bookmark;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


// JWT

@Repository
@RequiredArgsConstructor
public class JdbcTemplateBookmarkRepository implements BookmarkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Bookmark bookmark) {
        String sql = "INSERT INTO issue_user (member_email, issue_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookmark.getMemberEmail(), bookmark.getIssueId());
    }

    @Override
    public List<Bookmark> findByMemberEmail(String email) {
        String sql = """
        SELECT i.issue_id, i.issue_name, i.category
        FROM issue_user iu
        JOIN issue i ON iu.issue_id = i.issue_id
        WHERE iu.member_email = ?
    """;
        return jdbcTemplate.query(sql, bookmarkRowMapper(), email);
    }

    @Override
    public void deleteByMemberEmailAndIssueId(String email, Long issueId) {
        String sql = "DELETE FROM issue_user WHERE member_email = ? AND issue_id = ?";
        jdbcTemplate.update(sql, email, issueId);
    }

    private RowMapper<Bookmark> bookmarkRowMapper() {
        return (rs, rowNum) -> {
            Bookmark bookmark = new Bookmark();
            bookmark.setIssueId(rs.getLong("issue_id"));
            bookmark.setIssueName(rs.getString("issue_name"));
            bookmark.setCategory(rs.getString("category"));
            return bookmark;
        };
    }
}
