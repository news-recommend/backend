package news_recommend.news.bookmark.dto;


// 자세한 북마크 정보 출력
//public class BookmarkResponseDto {
//    private Long bookmarkId;
//    private Long userId;
//    private IssueDto issue;
//
//    public BookmarkResponseDto(Long bookmarkId, Long userId, IssueDto issue) {
//        this.bookmarkId = bookmarkId;
//        this.userId = userId;
//        this.issue = issue;
//    }
//
//    public static class IssueDto {
//        private Long issueId;
//        private String issueName;
//        private String category;
//        private String emotion;
//
//        public IssueDto(Long issueId, String issueName, String category, String emotion) {
//            this.issueId = issueId;
//            this.issueName = issueName;
//            this.category = category;
//            this.emotion = emotion;
//        }
//
//        // Getter 추가 (필요하면 Setter도)
//        public Long getIssueId() { return issueId; }
//        public String getIssueName() { return issueName; }
//        public String getCategory() { return category; }
//        public String getEmotion() { return emotion; }
//    }
//
//    // Getter 추가
//    public Long getBookmarkId() { return bookmarkId; }
//    public Long getUserId() { return userId; }
//    public IssueDto getIssue() { return issue; }
//}

//JWT

import lombok.AllArgsConstructor;
import lombok.Getter;
import news_recommend.news.bookmark.Bookmark;

@Getter
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long id;
    private String memberEmail;
    private Long issueId;

    public static BookmarkResponseDto fromEntity(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getId(),
                bookmark.getMemberEmail(),
                bookmark.getIssueId()
        );
    }
}
