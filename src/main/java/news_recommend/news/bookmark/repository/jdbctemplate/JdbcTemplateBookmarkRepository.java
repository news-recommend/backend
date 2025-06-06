package news_recommend.news.bookmark.repository.jdbctemplate;


import lombok.RequiredArgsConstructor;
import news_recommend.news.bookmark.dto.BookmarkResponseDto;
import news_recommend.news.bookmark.repository.BookmarkRepository;
import news_recommend.news.bookmark.Bookmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


//public class JdbcTemplateBookmarkRepository implements BookmarkRepository {
//
//    public static final Logger log = LoggerFactory.getLogger(JdbcTemplateBookmarkRepository.class);
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public JdbcTemplateBookmarkRepository(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    @Override
//    public Bookmark save(Bookmark bookmark) {
//        String sql = "INSERT INTO issue_user(user_id, issue_id) VALUES (?, ?) RETURNING id";
//        Long bookmarkId = jdbcTemplate.queryForObject(sql, new Object[]{
//                bookmark.getUserId(),
//                bookmark.getIssueId()
//        }, Long.class);
//
//        bookmark.setBookmarkId(bookmarkId);
//        return bookmark;
//    }
//
//    @Override
//    public void deleteByUserAndIssue(Long userId, Long issueId) {
//        String sql = "DELETE FROM issue_user WHERE user_id = ? AND issue_id = ?";
//        jdbcTemplate.update(sql, userId, issueId);
//    }
//
//    @Override
//    public List<Bookmark> findByUserId(Long userId) {
//        String sql = "SELECT id, user_id, issue_id FROM issue_user WHERE user_id = ?";
//        return jdbcTemplate.query(sql, bookmarkRowMapper(), userId);
//    }
//
//    private RowMapper<Bookmark> bookmarkRowMapper() {
//        return (rs, rowNum) -> new Bookmark(
//                rs.getLong("id"),
//                rs.getLong("user_id"),
//                rs.getLong("issue_id")
//        );
//    }
//
//    // 자세한 북마크 리스트 출력
//    @Override
//    public List<BookmarkResponseDto> findDetailedByUserId(Long userId) {
//        String sql = "SELECT b.id AS bookmark_id, b.user_id, i.issue_id, i.issue_name, i.category, i.emotion " +
//                "FROM issue_user b " +
//                "JOIN issue i ON b.issue_id = i.issue_id " +
//                "WHERE b.user_id = ?";
//
//        return jdbcTemplate.query(sql, (rs, rowNum) -> new BookmarkResponseDto(
//                rs.getLong("bookmark_id"),
//                rs.getLong("user_id"),
//                new BookmarkResponseDto.IssueDto(
//                        rs.getLong("issue_id"),
//                        rs.getString("issue_name"),
//                        rs.getString("category"),
//                        rs.getString("emotion")
//                )
//        ), userId);
//    }
//}

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
        String sql = "SELECT * FROM issue_user WHERE member_email = ?";
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
            bookmark.setId(rs.getLong("id"));
            bookmark.setMemberEmail(rs.getString("member_email"));
            bookmark.setIssueId(rs.getLong("issue_id"));
            return bookmark;
        };
    }
}
