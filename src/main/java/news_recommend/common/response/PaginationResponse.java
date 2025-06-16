package news_recommend.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationResponse {
    private int currentPage;
    private int pageSize;
    private int totalItems;
    private int totalPages;
    private boolean hasNext;
}