package news_recommend.news.config;


import news_recommend.news.member.MemberService;
import news_recommend.news.member.repository.MemberRepository;
import news_recommend.news.member.repository.jdbctemplate.JdbcTemplateMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    private final DataSource dataSource;


    @Autowired
    public JdbcTemplateConfig(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public MemberRepository accountRepository() {
        return new JdbcTemplateMemberRepository(dataSource);
    }

    @Bean
    public MemberService accountService() {
        return new MemberService(accountRepository());
    }

}
