package news_recommend.news.bookmark;


import news_recommend.news.bookmark.dto.BookmarkResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // 북마크 추가
    @PostMapping
    public ResponseEntity<Bookmark> addBookmark(@RequestBody Bookmark bookmark) {
        return ResponseEntity.ok(bookmarkService.addBookmark(bookmark));
    }

    // 북마크 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteBookmark(@RequestParam Long userId, @RequestParam Long issueId) {
        bookmarkService.deleteByUserAndIssue(userId, issueId);
        return ResponseEntity.ok("북마크가 성공적으로 삭제되었습니다.");
    }

    // 내 북마크 리스트 조회
    @GetMapping
    public ResponseEntity<List<Bookmark>> getMyBookmarks(@RequestParam Long userId) {
        return ResponseEntity.ok(bookmarkService.getBookmarksByUserId(userId));
    }

    // 자세한 북마크 리스트 출력
    @GetMapping("/detailed")
    public ResponseEntity<List<BookmarkResponseDto>> getMyDetailedBookmarks(@RequestParam Long userId) {
        return ResponseEntity.ok(bookmarkService.getDetailedBookmarksByUserId(userId));
    }
}
