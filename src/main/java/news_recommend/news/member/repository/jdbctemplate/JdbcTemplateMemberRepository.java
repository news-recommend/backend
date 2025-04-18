package news_recommend.news.member.repository.jdbctemplate;


import news_recommend.news.member.Member;
import news_recommend.news.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


public class JdbcTemplateMemberRepository implements MemberRepository {

    public static final Logger log =  LoggerFactory.getLogger(JdbcTemplateMemberRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }


//    @Override
//    public Member save(Member account) {
//        return null;
//    }
//
//    @Override
//    public Optional<Member> findById(Long id) {
//        return Optional.empty();
//    }

    @Override
    public List<Member> findAll() {
        String sql =  "SELECT * FROM member";
        List<Member> memberList = jdbcTemplate.query(sql, rowMapper());
        log.debug("accountList: {}", memberList.toString());
        return memberList;
    }

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO member (name, email, interest_category) VALUES (?, ?, ?) RETURNING user_id";
        Long userId = jdbcTemplate.queryForObject(sql, new Object[]{
                member.getName(),
                member.getEmail(),
                member.getInterestCategory()
        }, Long.class);

        member.setUserId(userId);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE user_id = ?";
        List<Member> result = jdbcTemplate.query(sql, rowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public int update(Member member) {
        String sql = "UPDATE member SET name = ?, email = ?, interest_category = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql,
                member.getName(),
                member.getEmail(),
                member.getInterestCategory(),
                member.getUserId()
        );
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM member WHERE user_id = ?";
        return jdbcTemplate.update(sql, id);
    }


//    @Override
//    public int update(Member account) {
//        return 0;
//    }
//
//    @Override
//    public int delete(Long id) {
//        return 0;
//    }

    private RowMapper<Member> rowMapper() {
        return (rs, rowNum ) -> new Member(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("interest_category")
        );
    }
}