package news_recommend.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String resultType; // "SUCCESS" 또는 "FAIL"
    private Object error;      // 예외 메시지 등 (없을 경우 null)
    private T success;         // 성공 시 반환할 데이터

    // ✅ 정적 팩토리 메서드 추가
    public static <T> ApiResponse<T> success(T body) {
        return new ApiResponse<>("SUCCESS", null, body);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>("FAIL", message, null);
    }
}
