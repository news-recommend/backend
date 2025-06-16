package news_recommend.news.bookmark;


import lombok.RequiredArgsConstructor;
import news_recommend.news.bookmark.dto.BookmarkRequestDto;
import news_recommend.news.bookmark.dto.BookmarkResponseDto;
import news_recommend.news.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// JWT

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookmarkResponseDto>>> getBookmarks(Authentication authentication) {
        String email = authentication.getName();
        List<BookmarkResponseDto> result = bookmarkService.getBookmarks(email);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> addBookmark(@RequestBody BookmarkRequestDto dto, Authentication authentication) {
        String email = authentication.getName();
        bookmarkService.addBookmark(email, dto.getIssueId());

        Map<String, Object> result = Map.of(
                "message", "북마크 추가 성공",
                "issueId", dto.getIssueId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result));
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteBookmark(@PathVariable Long issueId, Authentication authentication) {
        String email = authentication.getName();
        bookmarkService.deleteBookmark(email, issueId);

        return ResponseEntity.ok(
                ApiResponse.success(Map.of("message", "북마크 삭제 성공"))
        );
    }
}
