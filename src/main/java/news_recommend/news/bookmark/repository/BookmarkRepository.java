package news_recommend.news.bookmark.repository;

import news_recommend.news.bookmark.Bookmark;
import news_recommend.news.bookmark.dto.BookmarkResponseDto;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {

    Bookmark save(Bookmark bookmark);

    void deleteByUserAndIssue(Long userId, Long issueId);

    List<Bookmark> findByUserId(Long userId); // 내 북마크 리스트

    // 자세한 북마크 정보 출력
    List<BookmarkResponseDto> findDetailedByUserId(Long userId);
}