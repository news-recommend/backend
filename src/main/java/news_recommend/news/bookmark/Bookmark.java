package news_recommend.news.bookmark;



public class Bookmark {

    private Long bookmarkId;   // PK
    private Long userId;       // FK (회원 ID)
    private Long issueId;      // FK (이슈 ID)

    public Bookmark() {}

    public Bookmark(Long userId, Long issueId) {
        this.userId = userId;
        this.issueId = issueId;
    }

    public Bookmark(Long bookmarkId, Long userId, Long issueId) {
        this.bookmarkId = bookmarkId;
        this.userId = userId;
        this.issueId = issueId;
    }

    public Long getBookmarkId() {
        return bookmarkId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getIssueId() {
        return issueId;
    }
    public void setBookmarkId(Long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
}