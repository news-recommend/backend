package news_recommend.news.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import news_recommend.news.bookmark.Bookmark;

@Getter
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long issueId;
    private String issueName;
    private String category;

    public static BookmarkResponseDto fromEntity(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getIssueId(),
                bookmark.getIssueName(),
                bookmark.getCategory()
        );
    }
}
