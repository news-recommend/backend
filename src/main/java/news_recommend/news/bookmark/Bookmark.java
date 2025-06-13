package news_recommend.news.bookmark;


// JWT

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bookmark {
    private Long id;
    private String memberEmail;  // ✅ 이메일 기반
    private Long issueId;
    private String issueName;
    private String category;

    public Bookmark() {}

    public Bookmark(String memberEmail, Long issueId) {
        this.memberEmail = memberEmail;
        this.issueId = issueId;
    }
}