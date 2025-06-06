package news_recommend.news.bookmark;

import lombok.RequiredArgsConstructor;
import news_recommend.news.bookmark.dto.BookmarkResponseDto;
import news_recommend.news.bookmark.repository.BookmarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


//public class BookmarkService {
//
//    private final BookmarkRepository bookmarkRepository;
//
//    public BookmarkService(BookmarkRepository bookmarkRepository) {
//        this.bookmarkRepository = bookmarkRepository;
//    }
//
//    public Bookmark addBookmark(Bookmark bookmark) {
//        return bookmarkRepository.save(bookmark);
//    }
//
//    public void deleteByUserAndIssue(Long userId, Long issueId) {
//        bookmarkRepository.deleteByUserAndIssue(userId, issueId);
//    }
//
//    public List<Bookmark> getBookmarksByUserId(Long userId) {
//        return bookmarkRepository.findByUserId(userId);
//    }
//
//    // 자세한 북마크 리스트 출력
//    public List<BookmarkResponseDto> getDetailedBookmarksByUserId(Long userId) {
//        return bookmarkRepository.findDetailedByUserId(userId);
//    }
//}

// JWT

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public void addBookmark(String email, Long issueId) {
        bookmarkRepository.save(new Bookmark(email, issueId));
    }

    public List<BookmarkResponseDto> getBookmarks(String email) {
        return bookmarkRepository.findByMemberEmail(email).stream()
                .map(BookmarkResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteBookmark(String email, Long issueId) {
        bookmarkRepository.deleteByMemberEmailAndIssueId(email, issueId);
    }
}
