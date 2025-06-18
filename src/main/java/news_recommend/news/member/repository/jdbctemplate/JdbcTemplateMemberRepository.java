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

    @Override  // 비밀번호 추가
    public Member save(Member member) {
        String sql = "INSERT INTO member (name, email, interest_category,  agegroup, gender, password) VALUES (?, ?, ?, ?, ?, ?) RETURNING user_id";
        Long userId = jdbcTemplate.queryForObject(sql, new Object[]{
                member.getName(),
                member.getEmail(),
                member.getInterestCategory(),
                member.getAgeGroup(),
                member.getGender(),
                member.getPassword()
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
        String sql = "UPDATE member SET name = ?, email = ?, interest_category = ?, agegroup = ?, gender = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql,
                member.getName(),
                member.getEmail(),
                member.getInterestCategory(),
                member.getAgeGroup(),
                 member.getGender(),
                member.getUserId()
        );
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM member WHERE user_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Member findMyProfile( ) {
        // 이거 인증단에서 어떻게 jwt 토큰 다루는지 체크하고 변경해야함
        String sql = "SELECT * FROM member WHERE user_id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper());
    }

    @Override
    public String findPasswordByUserId(Long userId) {
        String sql = "SELECT password FROM member WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, userId);
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        String sql = "UPDATE member SET password = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, newPassword, userId);
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

    //이미 존재하는 이메일로 회원가입하려는 경우.
    @Override
    public boolean existsByEmail(String email) { // 추가
        String sql = "SELECT COUNT(*) FROM member WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";

        List<Member> result = jdbcTemplate.query(sql, rowMapper(), email);


        return result.stream().findAny();
    }

// 비밀번호 추가
    private RowMapper<Member> rowMapper() {
        return (rs, rowNum ) -> new Member(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("interest_category"),
                rs.getString("agegroup"),
                rs.getString("gender"),
                rs.getString("password")
        );
    }
}