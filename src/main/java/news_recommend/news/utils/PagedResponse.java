package news_recommend.news.utils;

import java.util.List;

public class PagedResponse<T> {
    private Pagination pagination;
    private List<T> data;

    public PagedResponse(Pagination pagination, List<T> data) {
        this.pagination = pagination;
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public List<T> getData() {
        return data;
    }
}
