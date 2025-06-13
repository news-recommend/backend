package news_recommend.news.bookmark;

import lombok.RequiredArgsConstructor;
import news_recommend.news.bookmark.dto.BookmarkResponseDto;
import news_recommend.news.bookmark.repository.BookmarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
