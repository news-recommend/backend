package news_recommend.news.utils;

// ApiResponse.java
public class ApiResponse<T> {

    private String resultType; // "SUCCESS" or "ERROR"
    private T success;            // 성공시 데이터
    private ApiError error;    // 실패시 에러

    public ApiResponse(String resultType, T success, ApiError error) {
        this.resultType = resultType;
        this.success = success;
        this.error = error;
    }

    // 성공 응답
    public static <T> ApiResponse<T> success(T success) {
        return new ApiResponse<>("SUCCESS", success, null);
    }

    // 에러 응답
    public static <T> ApiResponse<T> error(String reason, String title) {
        return new ApiResponse<>("ERROR", null, new ApiError(reason, title));
    }

    // 단순 메시지 기반 실패 응답 (common 방식 지원용)
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>("FAIL", null, new ApiError(message, null));
    }




    // getter

    public String getResultType() {
        return resultType.equals("FAIL") ? "ERROR" : resultType;
    }

    public T getSuccess() {
        return success;
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
