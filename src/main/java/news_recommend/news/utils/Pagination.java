package news_recommend.news.utils;

public class Pagination {
    private int currentPage;
    private int pageSize;
    private int totalItems;
    private int totalPages;
    private boolean hasNext;

    public Pagination(int currentPage, int pageSize, int totalItems, int totalPages, boolean hasNext) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
