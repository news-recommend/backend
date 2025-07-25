package news_recommend.news.config;


import news_recommend.news.bookmark.BookmarkService;
import news_recommend.news.bookmark.repository.BookmarkRepository;
import news_recommend.news.bookmark.repository.jdbctemplate.JdbcTemplateBookmarkRepository;
import news_recommend.news.issue.IssueService;
import news_recommend.news.issue.repository.IssueRepository;
import news_recommend.news.issue.repository.NewsRepository;
import news_recommend.news.issue.repository.jdbctemplate.JdbcTemplateIssueRepository;
import news_recommend.news.issue.repository.jdbctemplate.JdbcTemplateNewsRepository;
import news_recommend.news.issue.service.NewsFetchService;
import news_recommend.news.jwt.JwtTokenProvider;
import news_recommend.news.llm.LLMService;
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
    public MemberRepository memberRepository() {
        return new JdbcTemplateMemberRepository(dataSource);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() { return new JwtTokenProvider();}

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository(), jwtTokenProvider());
    }  // jwtTokenProvider() 인자 추가

    @Bean
    public IssueRepository issueRepository() {
        return new JdbcTemplateIssueRepository(dataSource);
    }

    @Bean
    public IssueService issueService() {
        return new IssueService(
                issueRepository(),
                newsRepository(jdbcTemplate()),
                newsFetchService(),
                llmService()
        );
    }


    @Bean
    public BookmarkRepository bookmarkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateBookmarkRepository(jdbcTemplate); // ✅
    }

    @Bean
    public BookmarkService bookmarkService(BookmarkRepository bookmarkRepository) {
        return new BookmarkService(bookmarkRepository); // ✅ OK
    }

    @Bean
    public NewsRepository newsRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateNewsRepository(jdbcTemplate);  //
    }

    @Bean
    public NewsFetchService newsFetchService() {
        return new NewsFetchService();  // 혹은 생성자에 필요한 값 넣기
    }

    @Bean
    public LLMService llmService() {
        return new LLMService();  // 마찬가지로 필요한 의존성 있으면 주입
    }

}

