package news_recommend.news.utils;

// ApiResponse.java
public class ApiResponse<T> {

    private String resultType; // "SUCCESS" or "ERROR"
    private T data;            // 성공시 데이터
    private ApiError error;    // 실패시 에러

    private ApiResponse(String resultType, T data, ApiError error) {
        this.resultType = resultType;
        this.data = data;
        this.error = error;
    }

    // 성공 응답
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data, null);
    }

    // 에러 응답
    public static <T> ApiResponse<T> error(String reason, String title) {
        return new ApiResponse<>("ERROR", null, new ApiError(reason, title));
    }

    // getter
    public String getResultType() {
        return resultType;
    }

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    // 내부 에러 클래스
    public static class ApiError {
        private String reason;
        private String title;

        public ApiError(String reason, String title) {
            this.reason = reason;
            this.title = title;
        }

        public String getReason() {
            return reason;
        }

        public String getTitle() {
            return title;
        }
    }
}
